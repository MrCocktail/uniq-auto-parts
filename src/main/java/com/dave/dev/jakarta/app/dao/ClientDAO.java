package com.dave.dev.jakarta.app.dao;

import com.dave.dev.jakarta.app.models.Client;
import com.dave.dev.jakarta.app.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class ClientDAO {

    public List<Client> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Client c ORDER BY c.dateInscription DESC", Client.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Client findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public Client findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Client> search(String term) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String normalized = term == null ? "" : term.trim().toLowerCase();
            String likeTerm = "%" + normalized + "%";
            return em.createQuery(
                            "SELECT c FROM Client c "
                                    + "WHERE LOWER(c.nom) LIKE :term "
                                    + "OR LOWER(c.prenom) LIKE :term "
                                    + "OR LOWER(c.email) LIKE :term "
                                    + "OR LOWER(c.telephone) LIKE :term "
                                    + "ORDER BY c.dateInscription DESC",
                            Client.class)
                    .setParameter("term", likeTerm)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Client save(Client client) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
            return client;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public Client update(Client client) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Client merged = em.merge(client);
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
            Client client = em.find(Client.class, id);
            if (client != null) {
                em.remove(client);
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
