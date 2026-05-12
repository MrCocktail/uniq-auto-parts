package com.dave.dev.jakarta.app.dao;

import com.dave.dev.jakarta.app.models.Stock;
import com.dave.dev.jakarta.app.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class StockDAO {

    public List<Stock> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM Stock s JOIN FETCH s.piece p ORDER BY p.nom", Stock.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Stock findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Stock.class, id);
        } finally {
            em.close();
        }
    }

    public Stock findByPieceId(Long pieceId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT s FROM Stock s JOIN FETCH s.piece p WHERE p.id = :pieceId", Stock.class)
                    .setParameter("pieceId", pieceId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public Stock save(Stock stock) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(stock);
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

    public Stock update(Stock stock) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Stock merged = em.merge(stock);
            em.getTransaction().commit();
            return merged;
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
