package com.dave.dev.jakarta.app.servlets;

import com.dave.dev.jakarta.app.models.Client;
import com.dave.dev.jakarta.app.services.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/clients", "/clients/new", "/clients/create", "/clients/edit", "/clients/update", "/clients/delete"})
public class ClientServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/clients/new".equals(path)) {
            req.setAttribute("client", new Client());
            req.setAttribute("formAction", "create");
            req.getRequestDispatcher("/WEB-INF/jsp/clients/form.jsp").forward(req, resp);
            return;
        }
        if ("/clients/edit".equals(path)) {
            Long id = parseId(req.getParameter("id"));
            Client client = id == null ? null : clientService.findById(id);
            if (client == null) {
                resp.sendRedirect(req.getContextPath() + "/clients?error=notfound");
                return;
            }
            req.setAttribute("client", client);
            req.setAttribute("formAction", "update");
            req.getRequestDispatcher("/WEB-INF/jsp/clients/form.jsp").forward(req, resp);
            return;
        }

        String query = req.getParameter("q");
        boolean hasQuery = query != null && !query.trim().isEmpty();
        List<Client> clients = hasQuery ? clientService.searchClients(query) : clientService.findAll();
        req.setAttribute("clients", clients);
        req.setAttribute("query", hasQuery ? query.trim() : "");
        req.setAttribute("error", req.getParameter("error"));
        req.getRequestDispatcher("/WEB-INF/jsp/clients/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/clients/create".equals(path)) {
            handleCreate(req, resp);
            return;
        }
        if ("/clients/update".equals(path)) {
            handleUpdate(req, resp);
            return;
        }
        if ("/clients/delete".equals(path)) {
            handleDelete(req, resp);
        }
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Client formClient = buildClientFromRequest(req);
        try {
            clientService.createClient(
                    formClient.getNom(),
                    formClient.getPrenom(),
                    formClient.getSexe(),
                    formClient.getEmail(),
                    formClient.getTelephone(),
                    formClient.getAdresse()
            );
            resp.sendRedirect(req.getContextPath() + "/clients?created=1");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
            req.setAttribute("client", formClient);
            req.setAttribute("formAction", "create");
            req.getRequestDispatcher("/WEB-INF/jsp/clients/form.jsp").forward(req, resp);
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = parseId(req.getParameter("id"));
        Client formClient = buildClientFromRequest(req);
        formClient.setId(id);

        try {
            clientService.updateClient(
                    id,
                    formClient.getNom(),
                    formClient.getPrenom(),
                    formClient.getSexe(),
                    formClient.getEmail(),
                    formClient.getTelephone(),
                    formClient.getAdresse()
            );
            resp.sendRedirect(req.getContextPath() + "/clients?updated=1");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
            req.setAttribute("client", formClient);
            req.setAttribute("formAction", "update");
            req.getRequestDispatcher("/WEB-INF/jsp/clients/form.jsp").forward(req, resp);
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = parseId(req.getParameter("id"));
        if (id != null) {
            clientService.deleteClient(id);
        }
        resp.sendRedirect(req.getContextPath() + "/clients?deleted=1");
    }

    private Client buildClientFromRequest(HttpServletRequest req) {
        Client client = new Client();
        client.setNom(req.getParameter("nom"));
        client.setPrenom(req.getParameter("prenom"));
        client.setSexe(req.getParameter("sexe"));
        client.setEmail(req.getParameter("email"));
        client.setTelephone(req.getParameter("telephone"));
        client.setAdresse(req.getParameter("adresse"));
        return client;
    }

    private Long parseId(String raw) {
        try {
            return raw == null ? null : Long.valueOf(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
