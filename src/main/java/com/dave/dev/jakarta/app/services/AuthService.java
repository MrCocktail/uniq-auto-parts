package com.dave.dev.jakarta.app.services;

import com.dave.dev.jakarta.app.dao.EmployeeDAO;
import com.dave.dev.jakarta.app.models.Employee;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final EmployeeDAO employeeDAO;

    public AuthService() {
        this.employeeDAO = new EmployeeDAO();
    }

    public AuthService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public Employee signup(String username, String email, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Le username est obligatoire.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caracteres.");
        }
        if (employeeDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Ce username existe deja.");
        }
        if (employeeDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Cet email existe deja.");
        }

        Employee employee = new Employee();
        employee.setUsername(username.trim());
        employee.setEmail(email.trim().toLowerCase());
        employee.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        return employeeDAO.save(employee);
    }

    public Employee login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        Employee employee = employeeDAO.findByUsername(username.trim());
        if (employee == null || !employee.isActive()) {
            return null;
        }

        if (!BCrypt.checkpw(password, employee.getPasswordHash())) {
            return null;
        }

        return employee;
    }
}
