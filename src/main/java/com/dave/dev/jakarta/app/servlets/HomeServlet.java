package com.dave.dev.jakarta.app.servlets;

import com.dave.dev.jakarta.app.models.Employee;
import com.dave.dev.jakarta.app.services.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@WebServlet(urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Employee currentUser = (Employee) req.getSession().getAttribute("currentUser");
        req.setAttribute("currentUser", currentUser);

        try {
            req.setAttribute("stats", dashboardService.loadStats());
            req.setAttribute("pieces", dashboardService.findTopPieces(5));
            req.setAttribute("ventes", dashboardService.findRecentVentes(5));
        } catch (RuntimeException ex) {
            req.setAttribute("dashboardError", "Connexion a la base impossible. Verifiez MySQL et la configuration JDBC.");
            req.setAttribute("stats", new DashboardService.DashboardStats(0, 0, 0, java.math.BigDecimal.ZERO));
            req.setAttribute("pieces", Collections.emptyList());
            req.setAttribute("ventes", Collections.emptyList());
        }

        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
