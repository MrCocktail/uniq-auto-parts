<%@ page import="com.dave.dev.jakarta.app.models.Stock" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Stock - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-50 text-slate-800">
<%
    List<Stock> stocks = (List<Stock>) request.getAttribute("stocks");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<header class="border-b border-slate-200 bg-white">
    <div class="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-3 px-6 py-4">
        <div>
            <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">Inventaire</p>
            <h1 class="text-2xl font-bold text-slate-900">Stock</h1>
        </div>
        <nav class="flex flex-wrap items-center gap-3 text-sm font-semibold text-slate-600">
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/home">Dashboard</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/pieces">Pieces</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/clients">Clients</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/orders">Commandes</a>
            <a class="text-slate-900" href="${pageContext.request.contextPath}/stock">Stock</a>
        </nav>
    </div>
</header>

<main class="mx-auto max-w-6xl px-6 py-8">
    <% if (errorMessage != null) { %>
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        <%= errorMessage %>
    </div>
    <% } %>

    <div class="overflow-x-auto rounded-2xl border border-slate-200 bg-white shadow-sm">
        <table class="w-full text-sm">
            <thead class="bg-slate-50 text-left text-xs uppercase tracking-wide text-slate-500">
            <tr>
                <th class="px-4 py-3">ID</th>
                <th class="px-4 py-3">Date maj</th>
                <th class="px-4 py-3">Quantite disponible</th>
                <th class="px-4 py-3">Quantite reservee</th>
                <th class="px-4 py-3">Piece</th>
                <th class="px-4 py-3">Action</th>
            </tr>
            </thead>
            <tbody>
            <% if (stocks != null && !stocks.isEmpty()) { %>
            <% for (Stock stock : stocks) { %>
            <tr class="border-t border-slate-100">
                <td class="px-4 py-3 font-semibold"><%= stock.getId() %></td>
                <td class="px-4 py-3"><%= stock.getDateMaj() %></td>
                <td class="px-4 py-3">
                    <input class="w-20 rounded border border-slate-300 px-2 py-1 text-sm" form="stock-form-<%= stock.getId() %>" name="quantiteDisponible" type="number" min="0" required value="<%= stock.getQuantiteDisponible() == null ? 0 : stock.getQuantiteDisponible() %>">
                </td>
                <td class="px-4 py-3">
                    <input class="w-20 rounded border border-slate-300 px-2 py-1 text-sm" form="stock-form-<%= stock.getId() %>" name="quantiteReservee" type="number" min="0" required value="<%= stock.getQuantiteReservee() == null ? 0 : stock.getQuantiteReservee() %>">
                </td>
                <td class="px-4 py-3"><%= stock.getPiece().getNom() %></td>
                <td class="px-4 py-3">
                    <form id="stock-form-<%= stock.getId() %>" method="post" action="${pageContext.request.contextPath}/stock/update">
                        <input type="hidden" name="stockId" value="<%= stock.getId() %>">
                        <button class="rounded-lg bg-slate-800 px-3 py-1.5 text-xs font-semibold text-white hover:bg-slate-700" type="submit">Mettre a jour</button>
                    </form>
                </td>
            </tr>
            <% } %>
            <% } else { %>
            <tr>
                <td class="px-4 py-6 text-center text-slate-500" colspan="6">Aucun stock disponible.</td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
