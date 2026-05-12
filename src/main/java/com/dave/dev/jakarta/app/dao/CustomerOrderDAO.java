package com.dave.dev.jakarta.app.dao;

import com.dave.dev.jakarta.app.models.CustomerOrder;
import com.dave.dev.jakarta.app.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerOrderDAO {

    public List<CustomerOrder> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT o FROM CustomerOrder o JOIN FETCH o.client c ORDER BY o.dateCommande DESC", CustomerOrder.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public CustomerOrder findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(CustomerOrder.class, id);
        } finally {
            em.close();
        }
    }

    public CustomerOrder findByIdWithLines(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM CustomerOrder o JOIN FETCH o.client c LEFT JOIN FETCH o.lignes l LEFT JOIN FETCH l.piece p WHERE o.id = :id",
                            CustomerOrder.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<CustomerOrder> findByDateRange(LocalDateTime start, LocalDateTime end) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM CustomerOrder o JOIN FETCH o.client c "
                                    + "WHERE o.dateCommande >= :start AND o.dateCommande < :end "
                                    + "ORDER BY o.dateCommande DESC",
                            CustomerOrder.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public CustomerOrder save(CustomerOrder order) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
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

    public CustomerOrder update(CustomerOrder order) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            CustomerOrder merged = em.merge(order);
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
