package com.dave.dev.jakarta.app.servlets;

import com.dave.dev.jakarta.app.models.Stock;
import com.dave.dev.jakarta.app.services.StockService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/stock", "/stock/update"})
public class StockServlet extends HttpServlet {

    private final StockService stockService = new StockService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Stock> stocks = stockService.findAll();
        req.setAttribute("stocks", stocks);
        req.getRequestDispatcher("/WEB-INF/jsp/stock/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long stockId = parseId(req.getParameter("stockId"));
        Integer disponible = parseInt(req.getParameter("quantiteDisponible"));
        Integer reservee = parseInt(req.getParameter("quantiteReservee"));

        try {
            stockService.updateStock(stockId, disponible, reservee);
            resp.sendRedirect(req.getContextPath() + "/stock?updated=1");
        } catch (IllegalArgumentException ex) {
            List<Stock> stocks = stockService.findAll();
            req.setAttribute("stocks", stocks);
            req.setAttribute("errorMessage", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/stock/index.jsp").forward(req, resp);
        }
    }

    private Long parseId(String raw) {
        try {
            return raw == null ? null : Long.valueOf(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Integer parseInt(String raw) {
        if (raw == null || raw.isBlank()) {
            return 0;
        }
        try {
            return Integer.valueOf(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
