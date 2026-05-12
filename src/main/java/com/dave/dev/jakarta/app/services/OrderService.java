package com.dave.dev.jakarta.app.services;

import com.dave.dev.jakarta.app.dao.CustomerOrderDAO;
import com.dave.dev.jakarta.app.models.Client;
import com.dave.dev.jakarta.app.models.CustomerOrder;
import com.dave.dev.jakarta.app.models.CustomerOrderLine;
import com.dave.dev.jakarta.app.models.Employee;
import com.dave.dev.jakarta.app.models.OrderStatus;
import com.dave.dev.jakarta.app.models.Piece;
import com.dave.dev.jakarta.app.models.Stock;
import com.dave.dev.jakarta.app.util.JPAUtil;
import com.dave.dev.jakarta.app.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final CustomerOrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new CustomerOrderDAO();
    }

    public List<CustomerOrder> findAll() {
        return orderDAO.findAll();
    }

    public List<CustomerOrder> findByDate(LocalDate date) {
        if (date == null) {
            return List.of();
        }
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return orderDAO.findByDateRange(start, end);
    }

    public BigDecimal calculateTotal(List<CustomerOrder> orders) {
        if (orders == null || orders.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return orders.stream()
                .map(CustomerOrder::getMontantTotal)
                .filter(total -> total != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CustomerOrder findByIdWithLines(Long id) {
        return orderDAO.findByIdWithLines(id);
    }

    public CustomerOrder createOrder(Long clientId, Long employeId, List<LineInput> lines) {
        if (clientId == null) {
            throw new IllegalArgumentException("Le client est obligatoire.");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Ajoutez au moins une ligne de commande.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Client client = em.find(Client.class, clientId);
            if (client == null) {
                throw new IllegalArgumentException("Client introuvable.");
            }

            Employee employe = null;
            if (employeId != null) {
                employe = em.find(Employee.class, employeId);
            }

            CustomerOrder order = new CustomerOrder();
            order.setClient(client);
            order.setEmploye(employe);
            order.setDateCommande(LocalDateTime.now());
            order.setStatut(OrderStatus.EN_ATTENTE);

            List<CustomerOrderLine> orderLines = new ArrayList<>();
            for (LineInput line : lines) {
                ValidationUtil.requirePositiveInt(line.quantite(), "La quantite doit etre superieure a 0.");
                Piece piece = em.find(Piece.class, line.pieceId());
                if (piece == null) {
                    throw new IllegalArgumentException("Piece introuvable.");
                }

                Stock stock = findStockByPiece(em, piece);
                if (stock == null) {
                    int available = safeQty(piece.getQuantite());
                    if (available < line.quantite()) {
                        throw new IllegalArgumentException("Stock insuffisant pour " + piece.getNom() + ".");
                    }
                } else if (stock.quantiteLibre() < line.quantite()) {
                    throw new IllegalArgumentException("Stock insuffisant pour " + piece.getNom() + ".");
                }

                CustomerOrderLine orderLine = new CustomerOrderLine();
                orderLine.setOrder(order);
                orderLine.setPiece(piece);
                orderLine.setQuantite(line.quantite());
                orderLine.setMontant(piece.getPrixUnitaire().multiply(java.math.BigDecimal.valueOf(line.quantite())));
                orderLines.add(orderLine);

                if (stock == null) {
                    piece.setQuantite(safeQty(piece.getQuantite()) - line.quantite());
                    em.merge(piece);
                } else {
                    stock.setQuantiteReservee(stock.getQuantiteReservee() + line.quantite());
                    stock.setDateMaj(LocalDateTime.now());
                    em.merge(stock);
                    syncPieceQuantite(em, stock);
                }
            }

            order.setLignes(orderLines);
            order.recalculateTotal();

            em.persist(order);
            em.getTransaction().commit();
            return order;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public CustomerOrder updateStatus(Long orderId, OrderStatus newStatus) {
        if (orderId == null || newStatus == null) {
            throw new IllegalArgumentException("Statut invalide.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            CustomerOrder order = em.createQuery(
                            "SELECT o FROM CustomerOrder o JOIN FETCH o.lignes l JOIN FETCH l.piece p WHERE o.id = :id",
                            CustomerOrder.class)
                    .setParameter("id", orderId)
                    .getSingleResult();

            if (order.getStatut() == OrderStatus.ANNULEE || order.getStatut() == OrderStatus.LIVREE) {
                throw new IllegalArgumentException("Le statut ne peut plus etre modifie.");
            }

            if (newStatus == OrderStatus.ANNULEE) {
                releaseReservedStock(em, order);
            } else if (newStatus == OrderStatus.LIVREE) {
                finalizeStock(em, order);
            }

            order.setStatut(newStatus);
            em.merge(order);
            em.getTransaction().commit();
            return order;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    private Stock findStockByPiece(EntityManager em, Piece piece) {
        return em.createQuery("SELECT s FROM Stock s WHERE s.piece = :piece", Stock.class)
                .setParameter("piece", piece)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    private void releaseReservedStock(EntityManager em, CustomerOrder order) {
        for (CustomerOrderLine line : order.getLignes()) {
            Stock stock = findStockByPiece(em, line.getPiece());
            if (stock == null) {
                Piece piece = em.find(Piece.class, line.getPiece().getId());
                if (piece != null) {
                    piece.setQuantite(safeQty(piece.getQuantite()) + line.getQuantite());
                    em.merge(piece);
                }
                continue;
            }
            stock.setQuantiteReservee(Math.max(0, stock.getQuantiteReservee() - line.getQuantite()));
            stock.setDateMaj(LocalDateTime.now());
            em.merge(stock);
            syncPieceQuantite(em, stock);
        }
    }

    private void finalizeStock(EntityManager em, CustomerOrder order) {
        for (CustomerOrderLine line : order.getLignes()) {
            Stock stock = findStockByPiece(em, line.getPiece());
            if (stock == null) {
                continue;
            }
            int reservedAfter = stock.getQuantiteReservee() - line.getQuantite();
            int availableAfter = stock.getQuantiteDisponible() - line.getQuantite();
            if (reservedAfter < 0 || availableAfter < 0) {
                throw new IllegalArgumentException("Stock negatif pour " + line.getPiece().getNom() + ".");
            }
            stock.setQuantiteReservee(reservedAfter);
            stock.setQuantiteDisponible(availableAfter);
            stock.setDateMaj(LocalDateTime.now());
            em.merge(stock);
            syncPieceQuantite(em, stock);
        }
    }

    public record LineInput(Long pieceId, Integer quantite) {
    }

    private int safeQty(Integer qty) {
        return qty == null ? 0 : qty;
    }

    private void syncPieceQuantite(EntityManager em, Stock stock) {
        Piece piece = stock.getPiece();
        if (piece == null) {
            return;
        }
        int available = safeQty(stock.getQuantiteDisponible()) - safeQty(stock.getQuantiteReservee());
        piece.setQuantite(Math.max(0, available));
        em.merge(piece);
    }
}
