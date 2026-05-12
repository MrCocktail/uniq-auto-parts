<%@ page import="com.dave.dev.jakarta.app.models.CustomerOrder" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Commandes - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-50 text-slate-800">
<%
    List<CustomerOrder> orders = (List<CustomerOrder>) request.getAttribute("orders");
    String error = (String) request.getAttribute("error");
    String dateError = (String) request.getAttribute("dateError");
    String reportDateValue = (String) request.getAttribute("reportDateValue");
    LocalDate reportDate = (LocalDate) request.getAttribute("reportDate");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.FRENCH);
    DateTimeFormatter reportFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH);
    boolean hasReport = reportDate != null;
%>
<header class="border-b border-slate-200 bg-white">
    <div class="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-3 px-6 py-4">
        <div>
            <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">Ventes</p>
            <h1 class="text-2xl font-bold text-slate-900">Commandes</h1>
        </div>
        <nav class="flex flex-wrap items-center gap-3 text-sm font-semibold text-slate-600">
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/home">Dashboard</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/pieces">Pieces</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/clients">Clients</a>
            <a class="text-slate-900" href="${pageContext.request.contextPath}/orders">Commandes</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/stock">Stock</a>
        </nav>
    </div>
</header>

<main class="mx-auto max-w-6xl px-6 py-8">
    <% if ("notfound".equals(error)) { %>
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        Commande introuvable.
    </div>
    <% } %>
    <% if (dateError != null) { %>
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        <%= dateError %>
    </div>
    <% } %>

    <div class="mb-5 flex flex-wrap items-center gap-3">
        <div class="w-full md:w-auto">
            <p class="text-sm text-slate-500">Total: <%= orders != null ? orders.size() : 0 %> commande(s)</p>
            <% if (hasReport) { %>
            <p class="text-xs text-slate-400">Rapport du <%= reportDate.format(reportFormatter) %></p>
            <% } %>
        </div>
        <div class="flex w-full md:flex-1 md:justify-center">
            <form class="flex flex-wrap items-center justify-center gap-2" method="get" action="${pageContext.request.contextPath}/orders">
                <input class="w-48 rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-slate-400 focus:outline-none" type="date" name="date" value="<%= reportDateValue == null ? "" : reportDateValue %>">
                <button class="rounded-lg bg-amber-200 px-3 py-2 text-sm font-semibold text-slate-900 hover:bg-amber-300" type="submit">Voir le rapport</button>
                <% if (hasReport || (reportDateValue != null && !reportDateValue.isBlank())) { %>
                <a class="rounded-lg border border-slate-200 px-3 py-2 text-sm font-semibold text-slate-600 hover:bg-slate-50" href="${pageContext.request.contextPath}/orders">Effacer</a>
                <% } %>
            </form>
        </div>
        <div class="w-full md:w-auto md:ml-auto">
            <a class="inline-flex rounded-lg bg-slate-800 px-4 py-2 text-white hover:bg-slate-700" href="${pageContext.request.contextPath}/orders/new">Nouvelle commande</a>
        </div>
    </div>

    <% if (hasReport) { %>
    <div class="mb-5 rounded-2xl border border-amber-100 bg-amber-50 px-5 py-4 text-sm text-amber-900">
        Total du jour: <span class="font-semibold"><%= request.getAttribute("dailyTotal") %> HTG</span>
    </div>
    <% } %>

    <div class="overflow-x-auto rounded-2xl border border-slate-200 bg-white shadow-sm">
        <table class="w-full text-sm">
            <thead class="bg-slate-50 text-left text-xs uppercase tracking-wide text-slate-500">
            <tr>
                <th class="px-4 py-3">Ref</th>
                <th class="px-4 py-3">Client</th>
                <th class="px-4 py-3">Date</th>
                <th class="px-4 py-3">Montant</th>
                <th class="px-4 py-3">Statut</th>
                <th class="px-4 py-3">Action</th>
            </tr>
            </thead>
            <tbody>
            <% if (orders != null && !orders.isEmpty()) { %>
            <% for (CustomerOrder order : orders) { %>
            <tr class="border-t border-slate-100">
                <td class="px-4 py-3 font-semibold">#<%= order.getId() %></td>
                <td class="px-4 py-3"><%= order.getClient().getPrenom() == null ? "" : order.getClient().getPrenom() %> <%= order.getClient().getNom() %></td>
                <td class="px-4 py-3"><%= order.getDateCommande() == null ? "-" : order.getDateCommande().format(dateFormatter) %></td>
                <td class="px-4 py-3"><%= order.getMontantTotal() %> HTG</td>
                <td class="px-4 py-3"><%= order.getStatut() %></td>
                <td class="px-4 py-3">
                    <a class="rounded-lg border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600 hover:bg-slate-50" href="${pageContext.request.contextPath}/orders/view?id=<%= order.getId() %>">Voir</a>
                </td>
            </tr>
            <% } %>
            <% } else { %>
            <tr>
                <td class="px-4 py-6 text-center text-slate-500" colspan="6">
                    <%= hasReport ? "Aucun resultat pour cette date." : "Aucune commande enregistree." %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
