<%@ page import="com.dave.dev.jakarta.app.models.Client" %>
<%@ page import="com.dave.dev.jakarta.app.models.Piece" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle commande - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-50 text-slate-800">
<%
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<Piece> pieces = (List<Piece>) request.getAttribute("pieces");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String selectedClient = request.getParameter("clientId");
    String[] pieceIds = request.getParameterValues("pieceId");
    String[] quantites = request.getParameterValues("quantite");
%>
<header class="border-b border-slate-200 bg-white">
    <div class="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <div>
            <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">Ventes</p>
            <h1 class="text-2xl font-bold text-slate-900">Nouvelle commande</h1>
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

    <form method="post" action="${pageContext.request.contextPath}/orders/create" class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <div class="mb-6">
            <label class="block text-sm font-semibold text-slate-700 mb-1" for="clientId">Client</label>
            <select id="clientId" name="clientId" required class="w-full rounded-lg border border-slate-300 px-3 py-2">
                <option value="">Selectionner un client</option>
                <% if (clients != null) { %>
                <% for (Client client : clients) { %>
                <option value="<%= client.getId() %>" <%= String.valueOf(client.getId()).equals(selectedClient) ? "selected" : "" %>>
                    <%= client.getPrenom() == null ? "" : client.getPrenom() %> <%= client.getNom() %> - <%= client.getEmail() %>
                </option>
                <% } %>
                <% } %>
            </select>
        </div>

        <div class="mb-4">
            <p class="text-sm font-semibold text-slate-700">Lignes de commande</p>
            <p class="text-xs text-slate-500">Ajoutez jusqu'a 5 pieces.</p>
        </div>

        <div class="grid gap-4">
            <% for (int i = 0; i < 5; i++) { %>
            <div class="grid gap-3 md:grid-cols-[1.2fr_0.4fr]">
                <div>
                    <label class="block text-xs font-semibold text-slate-500 mb-1">Piece</label>
                    <select name="pieceId" class="w-full rounded-lg border border-slate-300 px-3 py-2">
                        <option value="">Selectionner une piece</option>
                        <% if (pieces != null) { %>
                        <% for (Piece piece : pieces) { %>
                        <option value="<%= piece.getId() %>" <%= pieceIds != null && pieceIds.length > i && String.valueOf(piece.getId()).equals(pieceIds[i]) ? "selected" : "" %>>
                            <%= piece.getNom() %>
                        </option>
                        <% } %>
                        <% } %>
                    </select>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-500 mb-1">Quantite</label>
                    <input name="quantite" min="1" type="number" class="w-full rounded-lg border border-slate-300 px-3 py-2" value="<%= quantites != null && quantites.length > i ? quantites[i] : "" %>">
                </div>
            </div>
            <% } %>
        </div>

        <div class="mt-6 flex flex-wrap gap-3">
            <button class="rounded-lg bg-slate-800 px-4 py-2 text-white hover:bg-slate-700" type="submit">Creer la commande</button>
            <a class="rounded-lg border border-slate-200 px-4 py-2 text-slate-600 hover:bg-slate-50" href="${pageContext.request.contextPath}/orders">Annuler</a>
        </div>
    </form>
</main>
</body>
</html>
