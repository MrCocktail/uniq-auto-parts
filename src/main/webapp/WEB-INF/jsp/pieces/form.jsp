<%@ page import="com.dave.dev.jakarta.app.models.Piece" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Piece - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-50 text-slate-800">
<%
    Piece piece = (Piece) request.getAttribute("piece");
    String formAction = (String) request.getAttribute("formAction");
    String errorMessage = (String) request.getAttribute("errorMessage");
    boolean isUpdate = "update".equals(formAction);
%>
<header class="border-b border-slate-200 bg-white">
    <div class="mx-auto flex max-w-4xl items-center justify-between px-6 py-4">
        <div>
            <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500"><%= isUpdate ? "Modification" : "Creation" %></p>
            <h1 class="text-2xl font-bold text-slate-900">Piece</h1>
        </div>
        <a class="text-sm font-semibold text-slate-600 hover:text-slate-900" href="${pageContext.request.contextPath}/pieces">Retour liste</a>
    </div>
</header>

<main class="mx-auto max-w-4xl px-6 py-8">
    <% if (errorMessage != null) { %>
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        <%= errorMessage %>
    </div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/pieces/<%= formAction %>" class="grid gap-5 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <% if (isUpdate) { %>
        <input type="hidden" name="id" value="<%= piece.getId() %>">
        <% } %>
        <div>
            <label class="block text-sm font-semibold text-slate-700 mb-1" for="nom">Nom</label>
            <input id="nom" name="nom" required class="w-full rounded-lg border border-slate-300 px-3 py-2" type="text" value="<%= piece.getNom() == null ? "" : piece.getNom() %>">
        </div>
        <div>
            <label class="block text-sm font-semibold text-slate-700 mb-1" for="description">Description</label>
            <textarea id="description" name="description" rows="3" class="w-full rounded-lg border border-slate-300 px-3 py-2"><%= piece.getDescription() == null ? "" : piece.getDescription() %></textarea>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
            <div>
                <label class="block text-sm font-semibold text-slate-700 mb-1" for="prixUnitaire">Prix de vente (HTG)</label>
                <input id="prixUnitaire" name="prixUnitaire" required class="w-full rounded-lg border border-slate-300 px-3 py-2" type="number" step="0.01" min="0" value="<%= piece.getPrixUnitaire() == null ? "" : piece.getPrixUnitaire() %>">
            </div>
            <div>
                <label class="block text-sm font-semibold text-slate-700 mb-1" for="prixAchat">Prix d'achat (HTG)</label>
                <input id="prixAchat" name="prixAchat" class="w-full rounded-lg border border-slate-300 px-3 py-2" type="number" step="0.01" min="0" value="<%= piece.getPrixAchat() == null ? "" : piece.getPrixAchat() %>">
            </div>
        </div>
        <div class="grid gap-4 md:grid-cols-2">
            <div>
                <label class="block text-sm font-semibold text-slate-700 mb-1" for="quantite">Quantite</label>
                <input id="quantite" name="quantite" class="w-full rounded-lg border border-slate-300 px-3 py-2" type="number" min="0" value="<%= piece.getQuantite() == null ? "" : piece.getQuantite() %>">
            </div>
            <div>
                <label class="block text-sm font-semibold text-slate-700 mb-1" for="provenance">Provenance</label>
                <input id="provenance" name="provenance" class="w-full rounded-lg border border-slate-300 px-3 py-2" type="text" value="<%= piece.getProvenance() == null ? "" : piece.getProvenance() %>">
            </div>
        </div>
        <div>
            <label class="block text-sm font-semibold text-slate-700 mb-1" for="imageUrl">Image URL</label>
            <input id="imageUrl" name="imageUrl" class="w-full rounded-lg border border-slate-300 px-3 py-2" type="text" value="<%= piece.getImageUrl() == null ? "" : piece.getImageUrl() %>">
        </div>
        <div class="flex flex-wrap gap-3">
            <button class="rounded-lg bg-slate-800 px-4 py-2 text-white hover:bg-slate-700" type="submit">
                <%= isUpdate ? "Enregistrer" : "Creer" %>
            </button>
            <a class="rounded-lg border border-slate-200 px-4 py-2 text-slate-600 hover:bg-slate-50" href="${pageContext.request.contextPath}/pieces">Annuler</a>
        </div>
    </form>
</main>
</body>
</html>
