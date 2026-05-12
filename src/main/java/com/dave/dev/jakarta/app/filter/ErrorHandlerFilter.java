package com.dave.dev.jakarta.app.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class ErrorHandlerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);
        } catch (IllegalArgumentException ex) {
            forwardError(req, resp, 400, ex);
        } catch (Exception ex) {
            forwardError(req, resp, 500, ex);
        }
    }

    private void forwardError(HttpServletRequest req, HttpServletResponse resp, int status, Exception ex)
            throws IOException, ServletException {
        if (req.getAttribute("errorHandled") != null) {
            throw new ServletException(ex);
        }
        req.setAttribute("errorHandled", true);
        req.setAttribute("errorMessage", status == 400
                ? "Donnees invalides."
                : "Une erreur technique est survenue.");
        req.setAttribute("errorDetails", ex.getMessage());
        resp.setStatus(status);
        req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
    }
}
