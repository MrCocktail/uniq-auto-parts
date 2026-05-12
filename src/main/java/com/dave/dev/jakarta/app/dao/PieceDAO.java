package com.dave.dev.jakarta.app.dao;

import com.dave.dev.jakarta.app.models.Piece;
import com.dave.dev.jakarta.app.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class PieceDAO {

    public List<Piece> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try { 
                return em.createQuery("SELECT p FROM Piece p ", Piece.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Piece> search(String term, Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String normalized = term == null ? "" : term.trim().toLowerCase();
            String likeTerm = "%" + normalized + "%";
            String query = "SELECT p FROM Piece p "
                    + "WHERE LOWER(p.nom) LIKE :term "
                    + "OR LOWER(p.description) LIKE :term "
                    + "OR LOWER(p.provenance) LIKE :term";
            if (id != null) {
                query += " OR p.id = :id";
            }
            query += " ORDER BY p.nom ASC";

                TypedQuery<Piece> typedQuery = em.createQuery(query, Piece.class)
                    .setParameter("term", likeTerm);
            if (id != null) {
                typedQuery.setParameter("id", id);
            }
            return typedQuery.getResultList();
        } finally {
            em.close();
        }
    }

    public Piece findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Piece.class, id);
        } finally {
            em.close();
        }
    }

    public Piece save(Piece piece) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(piece);
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

    public Piece update(Piece piece) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Piece merged = em.merge(piece);
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

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Piece piece = em.find(Piece.class, id);
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
}
