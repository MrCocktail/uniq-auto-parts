package com.dave.dev.jakarta.app.services;

import com.dave.dev.jakarta.app.dao.StockDAO;
import com.dave.dev.jakarta.app.models.Piece;
import com.dave.dev.jakarta.app.models.Stock;
import com.dave.dev.jakarta.app.util.JPAUtil;
import com.dave.dev.jakarta.app.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class StockService {

    private final StockDAO stockDAO;

    public StockService() {
        this.stockDAO = new StockDAO();
    }

    public List<Stock> findAll() {
        return stockDAO.findAll();
    }

    public Stock updateStock(Long stockId, Integer disponible, Integer reservee) {
        ValidationUtil.requireNonNegativeInt(disponible, "La quantite disponible doit etre >= 0.");
        ValidationUtil.requireNonNegativeInt(reservee, "La quantite reservee doit etre >= 0.");
        if (reservee > disponible) {
            throw new IllegalArgumentException("La quantite reservee ne peut pas depasser la quantite disponible.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Stock stock = em.find(Stock.class, stockId);
            if (stock == null) {
                throw new IllegalArgumentException("Stock introuvable.");
            }

            stock.setQuantiteDisponible(disponible);
            stock.setQuantiteReservee(reservee);
            stock.setDateMaj(LocalDateTime.now());

            Piece piece = stock.getPiece();
            if (piece != null) {
                piece.setQuantite(Math.max(0, disponible - reservee));
                em.merge(piece);
            }

            em.merge(stock);
            em.getTransaction().commit();
            return stock;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}
