<%@ page import="com.dave.dev.jakarta.app.models.Client" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-50 text-slate-800">
<%
    Client client = (Client) request.getAttribute("client");
    String formAction = (String) request.getAttribute("formAction");
    String errorMessage = (String) request.getAttribute("errorMessage");
    boolean isUpdate = "update".equals(formAction);
%>
<header class="border-b border-slate-200 bg-white">
    <div class="mx-auto flex max-w-4xl items-center justify-between px-6 py-4">
        <div>
            <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500"><%= isUpdate ? "Modification" : "Creation" %></p>
            <h1 class="text-2xl font-bold text-slate-900">Client</h1>
        </div>
        <a class="text-sm font-semibold text-slate-600 hover:text-slate-900" href="${pageContext.request.contextPath}/clients">Retour liste</a>
    </div>
</header>

<main class="mx-auto max-w-4xl px-6 py-8">
    <% if (errorMessage != null) { %>
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        <%= errorMessage %>
    </div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/clients/<%= formAction %>" class="grid gap-5 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <% if (isUpdate) { %>
        <input type="hidden" name="id" value="<%= client.getId() %>">
        <% } %>
        <div class="grid gap-4 md:grid-cols-2">
            <div>
                <label class="block text-sm font-semibold text-slate-700 mb-1" for="nom">Nom</label>
                <input id="nom" name="nom" required class="w-full rounded-lg border border-slate-300 px-3 py-2" type="text" value="<%= client.getNom() == null ? "" : client.getNom() %>">
            </div>
            <div>
                <label class="block text-sm font-semibold text-slate-700 mb-1" for="prenom">Prenom</label>
                <input id="prenom" name="prenom" class="w-full rounded-lg border border-slate-300 px-3 py-2" type="text" value="<%= client.getPrenom() == null ? "" : client.getPrenom() %>">
            </div>
        </div>
        <div>
            <label class="block text-sm font-semibold text-slate-700 mb-1" for="sexe">Sexe</label>
            <select id="sexe" name="sexe" class="w-full rounded-lg border border-slate-300 px-3 py-2">
                <option value="" <%= client.getSexe() == null || client.getSexe().isBlank() ? "selected" : "" %>>Selectionner</option>
                <option value="M" <%= "M".equalsIgnoreCase(client.getSexe()) ? "selected" : "" %>>M</option>
                <option value="F" <%= "F".equalsIgnoreCase(client.getSexe()) ? "selected" : "" %>>F</option>
            </select>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
            <div>
                <label class="block text-sm font-semibold text-slate-700 mb-1" for="email">Email</label>
                <input id="email" name="email" required class="w-full rounded-lg border border-slate-300 px-3 py-2" type="email" value="<%= client.getEmail() == null ? "" : client.getEmail() %>">
            </div>
            <div>
                <label class="block text-sm font-semibold text-slate-700 mb-1" for="telephone">Telephone</label>
                <input id="telephone" name="telephone" class="w-full rounded-lg border border-slate-300 px-3 py-2" type="text" value="<%= client.getTelephone() == null ? "" : client.getTelephone() %>">
            </div>
        </div>
        <div>
            <label class="block text-sm font-semibold text-slate-700 mb-1" for="adresse">Adresse</label>
            <input id="adresse" name="adresse" class="w-full rounded-lg border border-slate-300 px-3 py-2" type="text" value="<%= client.getAdresse() == null ? "" : client.getAdresse() %>">
        </div>
        <div class="flex flex-wrap gap-3">
            <button class="rounded-lg bg-slate-800 px-4 py-2 text-white hover:bg-slate-700" type="submit">
                <%= isUpdate ? "Enregistrer" : "Creer" %>
            </button>
            <a class="rounded-lg border border-slate-200 px-4 py-2 text-slate-600 hover:bg-slate-50" href="${pageContext.request.contextPath}/clients">Annuler</a>
        </div>
    </form>
</main>
</body>
</html>
