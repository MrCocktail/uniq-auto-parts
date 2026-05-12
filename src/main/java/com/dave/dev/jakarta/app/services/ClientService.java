package com.dave.dev.jakarta.app.services;

import com.dave.dev.jakarta.app.dao.ClientDAO;
import com.dave.dev.jakarta.app.models.Client;
import com.dave.dev.jakarta.app.util.JPAUtil;
import com.dave.dev.jakarta.app.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

public class ClientService {

    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public List<Client> findAll() {
        return clientDAO.findAll();
    }

    public List<Client> searchClients(String term) {
        return clientDAO.search(term);
    }

    public Client findById(Long id) {
        return clientDAO.findById(id);
    }

    public Client createClient(String nom, String prenom, String sexe, String email, String telephone, String adresse) {
        ValidationUtil.requireNonBlank(nom, "Le nom est obligatoire.");
        ValidationUtil.requireEmail(email, "Email invalide.");

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (emailExists(em, email, null)) {
                throw new IllegalArgumentException("Cet email existe deja.");
            }

            Client client = new Client();
            client.setNom(nom.trim());
            client.setPrenom(prenom == null ? null : prenom.trim());
            client.setSexe(sexe == null || sexe.isBlank() ? null : sexe.trim());
            client.setEmail(email.trim().toLowerCase());
            client.setTelephone(telephone == null ? null : telephone.trim());
            client.setAdresse(adresse == null ? null : adresse.trim());
            client.setDateInscription(LocalDateTime.now());

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

    public Client updateClient(Long id, String nom, String prenom, String sexe, String email, String telephone, String adresse) {
        ValidationUtil.requireNonBlank(nom, "Le nom est obligatoire.");
        ValidationUtil.requireEmail(email, "Email invalide.");

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Client client = em.find(Client.class, id);
            if (client == null) {
                throw new IllegalArgumentException("Client introuvable.");
            }

            if (emailExists(em, email, id)) {
                throw new IllegalArgumentException("Cet email existe deja.");
            }

            client.setNom(nom.trim());
            client.setPrenom(prenom == null ? null : prenom.trim());
            client.setSexe(sexe == null || sexe.isBlank() ? null : sexe.trim());
            client.setEmail(email.trim().toLowerCase());
            client.setTelephone(telephone == null ? null : telephone.trim());
            client.setAdresse(adresse == null ? null : adresse.trim());

            em.merge(client);
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

    public void deleteClient(Long id) {
        clientDAO.delete(id);
    }

    private boolean emailExists(EntityManager em, String email, Long excludeId) {
        try {
            var query = em.createQuery(
                            "SELECT COUNT(c) FROM Client c WHERE c.email = :email" +
                                    (excludeId == null ? "" : " AND c.id <> :excludeId"),
                            Long.class)
                    .setParameter("email", email.trim().toLowerCase());
            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }
            Long count = query.getSingleResult();
            return count != null && count > 0;
        } catch (NoResultException ex) {
            return false;
        }
    }
}
