<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-100 flex items-center justify-center p-6">
<div class="w-full max-w-lg bg-white rounded-2xl shadow-lg p-8">
    <h1 class="text-2xl font-bold text-slate-800 mb-2">Erreur</h1>
    <p class="text-slate-600 mb-6">Une erreur est survenue pendant le traitement.</p>

    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% String errorDetails = (String) request.getAttribute("errorDetails"); %>
    <% if (errorMessage != null) { %>
    <div class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700 mb-4">
        <%= errorMessage %>
    </div>
    <% } %>
    <% if (errorDetails != null && !errorDetails.isBlank()) { %>
    <p class="text-sm text-slate-500">Details: <%= errorDetails %></p>
    <% } %>

    <div class="mt-6 flex flex-wrap gap-3">
        <a class="rounded-lg bg-slate-800 px-4 py-2 text-white hover:bg-slate-700" href="${pageContext.request.contextPath}/home">Retour au dashboard</a>
        <a class="rounded-lg border border-slate-200 px-4 py-2 text-slate-700 hover:bg-slate-50" href="javascript:history.back()">Retour</a>
    </div>
</div>
</body>
</html>
