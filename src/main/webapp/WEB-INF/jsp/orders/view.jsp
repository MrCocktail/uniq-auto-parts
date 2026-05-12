<%@ page import="com.dave.dev.jakarta.app.models.CustomerOrder" %>
<%@ page import="com.dave.dev.jakarta.app.models.CustomerOrderLine" %>
<%@ page import="com.dave.dev.jakarta.app.models.OrderStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Commande - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-50 text-slate-800">
<%
    CustomerOrder order = (CustomerOrder) request.getAttribute("order");
    String errorMessage = (String) request.getAttribute("errorMessage");
    List<CustomerOrderLine> lines = order != null ? order.getLignes() : null;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.FRENCH);
%>
<header class="border-b border-slate-200 bg-white">
    <div class="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <div>
            <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">Commande</p>
            <h1 class="text-2xl font-bold text-slate-900">#<%= order != null ? order.getId() : "-" %></h1>
        </div>
        <a class="text-sm font-semibold text-slate-600 hover:text-slate-900" href="${pageContext.request.contextPath}/orders">Retour liste</a>
    </div>
</header>

<main class="mx-auto max-w-6xl px-6 py-8">
    <% if (errorMessage != null) { %>
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        <%= errorMessage %>
    </div>
    <% } %>

    <% if (order != null) { %>
    <div class="grid gap-6 lg:grid-cols-[1.2fr_0.8fr]">
        <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
            <h2 class="text-lg font-bold text-slate-900 mb-4">Details</h2>
            <div class="grid gap-2 text-sm text-slate-600">
                <p><span class="font-semibold text-slate-700">Client:</span> <%= order.getClient().getPrenom() == null ? "" : order.getClient().getPrenom() %> <%= order.getClient().getNom() %></p>
                <p><span class="font-semibold text-slate-700">Date:</span> <%= order.getDateCommande() == null ? "-" : order.getDateCommande().format(dateFormatter) %></p>
                <p><span class="font-semibold text-slate-700">Montant:</span> <%= order.getMontantTotal() %> HTG</p>
                <p><span class="font-semibold text-slate-700">Statut:</span> <%= order.getStatut() %></p>
            </div>

            <div class="mt-6">
                <h3 class="text-sm font-semibold text-slate-700 mb-2">Mettre a jour le statut</h3>
                <form method="post" action="${pageContext.request.contextPath}/orders/status" class="flex flex-wrap gap-3">
                    <input type="hidden" name="orderId" value="<%= order.getId() %>">
                    <select name="status" class="rounded-lg border border-slate-300 px-3 py-2 text-sm">
                        <% for (OrderStatus status : OrderStatus.values()) { %>
                        <option value="<%= status.name() %>" <%= status == order.getStatut() ? "selected" : "" %>><%= status.name() %></option>
                        <% } %>
                    </select>
                    <button class="rounded-lg bg-slate-800 px-3 py-2 text-sm font-semibold text-white hover:bg-slate-700" type="submit">Mettre a jour</button>
                </form>
            </div>
        </div>

        <div class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
            <h2 class="text-lg font-bold text-slate-900 mb-4">Lignes</h2>
            <div class="overflow-x-auto">
                <table class="w-full text-sm">
                    <thead class="text-left text-xs uppercase tracking-wide text-slate-500">
                    <tr>
                        <th class="py-2">Piece</th>
                        <th class="py-2">Qt</th>
                        <th class="py-2">Prix</th>
                        <th class="py-2">Total</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (lines != null && !lines.isEmpty()) { %>
                    <% for (CustomerOrderLine line : lines) { %>
                    <tr class="border-t border-slate-100">
                        <td class="py-2"><%= line.getPiece().getNom() %></td>
                        <td class="py-2"><%= line.getQuantite() %></td>
                        <td class="py-2"><%= line.getPiece().getPrixUnitaire() %> HTG</td>
                        <td class="py-2 font-semibold"><%= line.getLineTotal() %> HTG</td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td class="py-4 text-slate-500" colspan="4">Aucune ligne.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <% } %>
</main>
</body>
</html>
