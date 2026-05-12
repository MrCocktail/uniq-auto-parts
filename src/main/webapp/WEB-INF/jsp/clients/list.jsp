<%@ page import="com.dave.dev.jakarta.app.models.Client" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clients - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-50 text-slate-800">
<%
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    String error = (String) request.getAttribute("error");
    String query = (String) request.getAttribute("query");
    boolean hasQuery = query != null && !query.trim().isEmpty();
%>
<header class="border-b border-slate-200 bg-white">
    <div class="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-3 px-6 py-4">
        <div>
            <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">CRM</p>
            <h1 class="text-2xl font-bold text-slate-900">Clients</h1>
        </div>
        <nav class="flex flex-wrap items-center gap-3 text-sm font-semibold text-slate-600">
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/home">Dashboard</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/pieces">Pieces</a>
            <a class="text-slate-900" href="${pageContext.request.contextPath}/clients">Clients</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/orders">Commandes</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/stock">Stock</a>
        </nav>
    </div>
</header>

<main class="mx-auto max-w-6xl px-6 py-8">
    <% if ("notfound".equals(error)) { %>
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        Client introuvable.
    </div>
    <% } %>

    <div class="mb-5 flex flex-wrap items-center gap-3">
        <div class="w-full md:w-auto">
            <p class="text-sm text-slate-500">Total: <%= clients != null ? clients.size() : 0 %> client(s)</p>
            <% if (hasQuery) { %>
            <p class="text-xs text-slate-400">Recherche: "<%= query %>"</p>
            <% } %>
        </div>
        <div class="flex w-full md:flex-1 md:justify-center">
            <form class="flex flex-wrap items-center justify-center gap-2" method="get" action="${pageContext.request.contextPath}/clients">
                <input class="w-56 rounded-lg border border-slate-200 px-3 py-2 text-sm focus:border-slate-400 focus:outline-none" type="search" name="q" placeholder="Nom, email, telephone" value="<%= hasQuery ? query : "" %>">
                <button class="rounded-lg bg-amber-200 px-3 py-2 text-sm font-semibold text-slate-900 hover:bg-amber-300" type="submit">Rechercher</button>
                <% if (hasQuery) { %>
                <a class="rounded-lg border border-slate-200 px-3 py-2 text-sm font-semibold text-slate-600 hover:bg-slate-50" href="${pageContext.request.contextPath}/clients">Effacer</a>
                <% } %>
            </form>
        </div>
        <div class="w-full md:w-auto md:ml-auto">
            <a class="inline-flex rounded-lg bg-slate-800 px-4 py-2 text-white hover:bg-slate-700" href="${pageContext.request.contextPath}/clients/new">Ajouter un client</a>
        </div>
    </div>

    <div class="overflow-x-auto rounded-2xl border border-slate-200 bg-white shadow-sm">
        <table class="w-full text-sm">
            <thead class="bg-slate-50 text-left text-xs uppercase tracking-wide text-slate-500">
            <tr>
                <th class="px-4 py-3">Nom</th>
                <th class="px-4 py-3">Sexe</th>
                <th class="px-4 py-3">Email</th>
                <th class="px-4 py-3">Telephone</th>
                <th class="px-4 py-3">Actions</th>
            </tr>
            </thead>
            <tbody>
            <% if (clients != null && !clients.isEmpty()) { %>
            <% for (Client client : clients) { %>
            <tr class="border-t border-slate-100">
                <td class="px-4 py-3 font-semibold"><%= client.getPrenom() == null ? "" : client.getPrenom() %> <%= client.getNom() %></td>
                <td class="px-4 py-3"><%= client.getSexe() == null ? "-" : client.getSexe() %></td>
                <td class="px-4 py-3"><%= client.getEmail() %></td>
                <td class="px-4 py-3"><%= client.getTelephone() == null ? "-" : client.getTelephone() %></td>
                <td class="px-4 py-3">
                    <div class="flex flex-wrap gap-2">
                        <a class="rounded-lg border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600 hover:bg-slate-50" href="${pageContext.request.contextPath}/clients/edit?id=<%= client.getId() %>">Modifier</a>
                        <form method="post" action="${pageContext.request.contextPath}/clients/delete" onsubmit="return confirm('Supprimer ce client ?');">
                            <input type="hidden" name="id" value="<%= client.getId() %>">
                            <button class="rounded-lg border border-red-200 px-3 py-1 text-xs font-semibold text-red-600 hover:bg-red-50" type="submit">Supprimer</button>
                        </form>
                    </div>
                </td>
            </tr>
            <% } %>
            <% } else { %>
            <tr>
                <td class="px-4 py-6 text-center text-slate-500" colspan="4">
                    <%= hasQuery ? "Aucun resultat." : "Aucun client enregistre." %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
