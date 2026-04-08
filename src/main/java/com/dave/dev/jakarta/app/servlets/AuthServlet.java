package com.dave.dev.jakarta.app.servlets;

import com.dave.dev.jakarta.app.models.Employee;
import com.dave.dev.jakarta.app.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/auth/login", "/auth/signup", "/auth/logout"})
public class AuthServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/auth/login".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
            return;
        }
        if ("/auth/signup".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/jsp/auth/signup.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/auth/login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/auth/login".equals(path)) {
            handleLogin(req, resp);
            return;
        }
        if ("/auth/signup".equals(path)) {
            handleSignup(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Employee employee = authService.login(username, password);
        if (employee == null) {
            req.setAttribute("error", "Identifiants invalides.");
            req.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("currentUser", employee);
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    private void handleSignup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (confirmPassword == null || !confirmPassword.equals(password)) {
            req.setAttribute("error", "La confirmation du mot de passe est invalide.");
            req.getRequestDispatcher("/WEB-INF/jsp/auth/signup.jsp").forward(req, resp);
            return;
        }

        try {
            Employee employee = authService.signup(username, email, password);
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", employee);
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/auth/signup.jsp").forward(req, resp);
        }
    }
}
