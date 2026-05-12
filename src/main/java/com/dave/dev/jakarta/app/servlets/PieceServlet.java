package com.dave.dev.jakarta.app.servlets;

import com.dave.dev.jakarta.app.models.Piece;
import com.dave.dev.jakarta.app.services.PieceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = {"/pieces", "/pieces/new", "/pieces/create", "/pieces/edit", "/pieces/update", "/pieces/delete"})
public class PieceServlet extends HttpServlet {

    private final PieceService pieceService = new PieceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/pieces/new".equals(path)) {
            req.setAttribute("piece", new Piece());
            req.setAttribute("formAction", "create");
            req.getRequestDispatcher("/WEB-INF/jsp/pieces/form.jsp").forward(req, resp);
            return;
        }
        if ("/pieces/edit".equals(path)) {
            Long id = parseId(req.getParameter("id"));
            Piece piece = id == null ? null : pieceService.findById(id);
            if (piece == null) {
                resp.sendRedirect(req.getContextPath() + "/pieces?error=notfound");
                return;
            }
            req.setAttribute("piece", piece);
            req.setAttribute("formAction", "update");
            req.getRequestDispatcher("/WEB-INF/jsp/pieces/form.jsp").forward(req, resp);
            return;
        }

        String query = req.getParameter("q");
        boolean hasQuery = query != null && !query.trim().isEmpty();
        List<Piece> pieces = hasQuery ? pieceService.searchPieces(query) : pieceService.findAll();
        req.setAttribute("pieces", pieces);
        req.setAttribute("query", hasQuery ? query.trim() : "");
        req.setAttribute("error", req.getParameter("error"));
        req.getRequestDispatcher("/WEB-INF/jsp/pieces/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/pieces/create".equals(path)) {
            handleCreate(req, resp);
            return;
        }
        if ("/pieces/update".equals(path)) {
            handleUpdate(req, resp);
            return;
        }
        if ("/pieces/delete".equals(path)) {
            handleDelete(req, resp);
        }
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Piece formPiece = buildPieceFromRequest(req);
        try {
            pieceService.createPiece(
                    formPiece.getNom(),
                    formPiece.getDescription(),
                    formPiece.getPrixUnitaire(),
                    formPiece.getPrixAchat(),
                    formPiece.getQuantite(),
                    formPiece.getProvenance(),
                    formPiece.getImageUrl()
            );
            resp.sendRedirect(req.getContextPath() + "/pieces?created=1");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
            req.setAttribute("piece", formPiece);
            req.setAttribute("formAction", "create");
            req.getRequestDispatcher("/WEB-INF/jsp/pieces/form.jsp").forward(req, resp);
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = parseId(req.getParameter("id"));
        Piece formPiece = buildPieceFromRequest(req);
        formPiece.setId(id);

        try {
            pieceService.updatePiece(
                    id,
                    formPiece.getNom(),
                    formPiece.getDescription(),
                    formPiece.getPrixUnitaire(),
                    formPiece.getPrixAchat(),
                    formPiece.getQuantite(),
                    formPiece.getProvenance(),
                    formPiece.getImageUrl()
            );
            resp.sendRedirect(req.getContextPath() + "/pieces?updated=1");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("errorMessage", ex.getMessage());
            req.setAttribute("piece", formPiece);
            req.setAttribute("formAction", "update");
            req.getRequestDispatcher("/WEB-INF/jsp/pieces/form.jsp").forward(req, resp);
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = parseId(req.getParameter("id"));
        if (id != null) {
            pieceService.deletePiece(id);
        }
        resp.sendRedirect(req.getContextPath() + "/pieces?deleted=1");
    }

    private Piece buildPieceFromRequest(HttpServletRequest req) {
        Piece piece = new Piece();
        piece.setNom(req.getParameter("nom"));
        piece.setDescription(req.getParameter("description"));
        piece.setProvenance(req.getParameter("provenance"));
        piece.setImageUrl(req.getParameter("imageUrl"));
        piece.setPrixUnitaire(parseBigDecimal(req.getParameter("prixUnitaire")));
        piece.setPrixAchat(parseBigDecimal(req.getParameter("prixAchat")));
        piece.setQuantite(parseInt(req.getParameter("quantite")));
        return piece;
    }

    private Long parseId(String raw) {
        try {
            return raw == null ? null : Long.valueOf(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(raw.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Integer parseInt(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(raw.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
