package com.dave.dev.jakarta.app.servlets;

import com.dave.dev.jakarta.app.models.Client;
import com.dave.dev.jakarta.app.models.CustomerOrder;
import com.dave.dev.jakarta.app.models.Employee;
import com.dave.dev.jakarta.app.models.OrderStatus;
import com.dave.dev.jakarta.app.models.Piece;
import com.dave.dev.jakarta.app.services.ClientService;
import com.dave.dev.jakarta.app.services.OrderService;
import com.dave.dev.jakarta.app.services.PieceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/orders", "/orders/new", "/orders/create", "/orders/view", "/orders/status"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final ClientService clientService = new ClientService();
    private final PieceService pieceService = new PieceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/orders/new".equals(path)) {
            loadOrderForm(req);
            req.getRequestDispatcher("/WEB-INF/jsp/orders/new.jsp").forward(req, resp);
            return;
        }
        if ("/orders/view".equals(path)) {
            Long id = parseId(req.getParameter("id"));
            CustomerOrder order = id == null ? null : orderService.findByIdWithLines(id);
            if (order == null) {
                resp.sendRedirect(req.getContextPath() + "/orders?error=notfound");
                return;
            }
            req.setAttribute("order", order);
            req.getRequestDispatcher("/WEB-INF/jsp/orders/view.jsp").forward(req, resp);
            return;
        }

        String dateRaw = req.getParameter("date");
        LocalDate reportDate = parseDate(dateRaw);
        boolean hasDate = reportDate != null;
        List<CustomerOrder> orders = hasDate ? orderService.findByDate(reportDate) : orderService.findAll();
        if (hasDate) {
            BigDecimal dailyTotal = orderService.calculateTotal(orders);
            req.setAttribute("dailyTotal", dailyTotal);
            req.setAttribute("reportDate", reportDate);
            req.setAttribute("reportDateValue", reportDate.format(DateTimeFormatter.ISO_DATE));
        } else if (dateRaw != null && !dateRaw.isBlank()) {
            req.setAttribute("dateError", "Date invalide.");
            req.setAttribute("reportDateValue", dateRaw.trim());
        }
        req.setAttribute("orders", orders);
        req.setAttribute("error", req.getParameter("error"));
        req.getRequestDispatcher("/WEB-INF/jsp/orders/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/orders/create".equals(path)) {
            handleCreate(req, resp);
            return;
        }
        if ("/orders/status".equals(path)) {
            handleStatus(req, resp);
        }
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long clientId = parseId(req.getParameter("clientId"));
        List<OrderService.LineInput> lines = parseLines(req.getParameterValues("pieceId"),
                req.getParameterValues("quantite"));

        Employee currentUser = (Employee) req.getSession().getAttribute("currentUser");
        Long employeId = currentUser == null ? null : currentUser.getRefEmploye();

        try {
            orderService.createOrder(clientId, employeId, lines);
            resp.sendRedirect(req.getContextPath() + "/orders?created=1");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
            loadOrderForm(req);
            req.getRequestDispatcher("/WEB-INF/jsp/orders/new.jsp").forward(req, resp);
        }
    }

    private void handleStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long orderId = parseId(req.getParameter("orderId"));
        String statusRaw = req.getParameter("status");

        try {
            OrderStatus status = statusRaw == null ? null : OrderStatus.valueOf(statusRaw);
            orderService.updateStatus(orderId, status);
            resp.sendRedirect(req.getContextPath() + "/orders/view?id=" + orderId + "&updated=1");
        } catch (IllegalArgumentException ex) {
            CustomerOrder order = orderId == null ? null : orderService.findByIdWithLines(orderId);
            req.setAttribute("order", order);
            req.setAttribute("errorMessage", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/orders/view.jsp").forward(req, resp);
        }
    }

    private void loadOrderForm(HttpServletRequest req) {
        List<Client> clients = clientService.findAll();
        List<Piece> pieces = pieceService.findAll();
        req.setAttribute("clients", clients);
        req.setAttribute("pieces", pieces);
    }

    private List<OrderService.LineInput> parseLines(String[] pieceIds, String[] quantites) {
        List<OrderService.LineInput> lines = new ArrayList<>();
        if (pieceIds == null || quantites == null) {
            return lines;
        }
        int count = Math.min(pieceIds.length, quantites.length);
        for (int i = 0; i < count; i++) {
            Long pieceId = parseId(pieceIds[i]);
            Integer qty = parseInt(quantites[i]);
            if (pieceId == null || qty == null) {
                continue;
            }
            lines.add(new OrderService.LineInput(pieceId, qty));
        }
        return lines;
    }

    private Long parseId(String raw) {
        try {
            return raw == null ? null : Long.valueOf(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Integer parseInt(String raw) {
        try {
            return raw == null ? null : Integer.valueOf(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(raw.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
