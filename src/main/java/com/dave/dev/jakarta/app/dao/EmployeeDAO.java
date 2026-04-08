package com.dave.dev.jakarta.app.dao;

import com.dave.dev.jakarta.app.models.Employee;
import com.dave.dev.jakarta.app.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class EmployeeDAO {

    public Employee save(Employee employee) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(employee);
            em.getTransaction().commit();
            return employee;
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public Employee findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employee e WHERE e.username = :username", Employee.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public Employee findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employee e WHERE e.email = :email", Employee.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
}
