package com.dave.dev.jakarta.app.services;

import com.dave.dev.jakarta.app.dao.PieceDAO;
import com.dave.dev.jakarta.app.models.Piece;
import com.dave.dev.jakarta.app.models.Stock;
import com.dave.dev.jakarta.app.util.JPAUtil;
import com.dave.dev.jakarta.app.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class PieceService {

    private final PieceDAO pieceDAO;

    public PieceService() {
        this.pieceDAO = new PieceDAO();
    }

    public List<Piece> findAll() {
        return pieceDAO.findAll();
    }

    public List<Piece> searchPieces(String term) {
        Long id = null;
        if (term != null) {
            try {
                id = Long.valueOf(term.trim());
            } catch (NumberFormatException ex) {
                id = null;
            }
        }
        return pieceDAO.search(term, id);
    }

    public Piece findById(Long id) {
        return pieceDAO.findById(id);
    }

    public Piece createPiece(String nom, String description, BigDecimal prixUnitaire, BigDecimal prixAchat,
                             Integer quantite, String provenance, String imageUrl) {
        ValidationUtil.requireNonBlank(nom, "Le nom est obligatoire.");
        ValidationUtil.requireNonNegative(prixUnitaire, "Le prix doit etre superieur ou egal a 0.");
        if (prixAchat != null && prixAchat.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le prix d'achat doit etre superieur ou egal a 0.");
        }
        if (quantite != null && quantite < 0) {
            throw new IllegalArgumentException("La quantite doit etre superieure ou egale a 0.");
        }

        int normalizedQuantite = quantite == null ? 0 : quantite;

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Piece piece = new Piece();
            piece.setNom(nom.trim());
            piece.setDescription(description == null ? null : description.trim());
            piece.setPrixUnitaire(prixUnitaire);
            piece.setPrixAchat(prixAchat);
            piece.setQuantite(normalizedQuantite);
            piece.setProvenance(provenance == null || provenance.isBlank() ? null : provenance.trim());
            piece.setImageUrl(imageUrl == null || imageUrl.isBlank() ? null : imageUrl.trim());

            em.persist(piece);

            Stock stock = new Stock();
            stock.setPiece(piece);
            stock.setQuantiteDisponible(normalizedQuantite);
            stock.setQuantiteReservee(0);
            stock.setDateMaj(java.time.LocalDateTime.now());
            em.persist(stock);

            em.getTransaction().commit();
            return piece;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public Piece updatePiece(Long id, String nom, String description, BigDecimal prixUnitaire, BigDecimal prixAchat,
                             Integer quantite, String provenance, String imageUrl) {
        ValidationUtil.requireNonBlank(nom, "Le nom est obligatoire.");
        ValidationUtil.requireNonNegative(prixUnitaire, "Le prix doit etre superieur ou egal a 0.");
        if (prixAchat != null && prixAchat.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le prix d'achat doit etre superieur ou egal a 0.");
        }
        if (quantite != null && quantite < 0) {
            throw new IllegalArgumentException("La quantite doit etre superieure ou egale a 0.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Piece piece = em.find(Piece.class, id);
            if (piece == null) {
                throw new IllegalArgumentException("Piece introuvable.");
            }

            piece.setNom(nom.trim());
            piece.setDescription(description == null ? null : description.trim());
            piece.setPrixUnitaire(prixUnitaire);
            piece.setPrixAchat(prixAchat);
            if (quantite != null) {
                piece.setQuantite(quantite);
            }
            piece.setProvenance(provenance == null || provenance.isBlank() ? null : provenance.trim());
            piece.setImageUrl(imageUrl == null || imageUrl.isBlank() ? null : imageUrl.trim());

            em.merge(piece);
            if (quantite != null) {
                syncStockFromPiece(em, piece, quantite);
            }
            em.getTransaction().commit();
            return piece;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public void deletePiece(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Piece piece = em.find(Piece.class, id);
            if (piece != null && piece.getStock() != null) {
                Stock stock = em.merge(piece.getStock());
                em.remove(stock);
            }
            if (piece != null) {
                em.remove(piece);
            }
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    private void syncStockFromPiece(EntityManager em, Piece piece, Integer quantite) {
        Stock stock = em.createQuery("SELECT s FROM Stock s WHERE s.piece = :piece", Stock.class)
                .setParameter("piece", piece)
                .getResultStream()
                .findFirst()
                .orElse(null);
        int reserved = stock == null ? 0 : safeQty(stock.getQuantiteReservee());
        int available = Math.max(0, quantite);

        if (stock == null) {
            Stock newStock = new Stock();
            newStock.setPiece(piece);
            newStock.setQuantiteReservee(reserved);
            newStock.setQuantiteDisponible(available + reserved);
            newStock.setDateMaj(java.time.LocalDateTime.now());
            em.persist(newStock);
        } else {
            stock.setQuantiteDisponible(available + reserved);
            stock.setDateMaj(java.time.LocalDateTime.now());
            em.merge(stock);
        }
    }

    private int safeQty(Integer qty) {
        return qty == null ? 0 : qty;
    }

}
