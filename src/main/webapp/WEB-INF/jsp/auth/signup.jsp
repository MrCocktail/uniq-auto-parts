<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - Uniq Auto Parts</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-100 flex items-center justify-center p-6">
<div class="w-full max-w-md bg-white rounded-2xl shadow-lg p-8">
    <h1 class="text-2xl font-bold text-slate-800 mb-2">Inscription</h1>
    <p class="text-slate-500 mb-6">Creez un compte employe.</p>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        <%= error %>
    </div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/auth/signup" class="space-y-4">
        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1" for="username">Nom d'utilisateur</label>
            <input id="username" name="username" required class="w-full rounded-lg border border-slate-300 px-3 py-2" type="text">
        </div>
        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1" for="email">Email</label>
            <input id="email" name="email" required class="w-full rounded-lg border border-slate-300 px-3 py-2" type="email">
        </div>
        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1" for="password">Mot de passe</label>
            <input id="password" name="password" minlength="6" required class="w-full rounded-lg border border-slate-300 px-3 py-2" type="password">
        </div>
        <div>
            <label class="block text-sm font-medium text-slate-700 mb-1" for="confirmPassword">Confirmer mot de passe</label>
            <input id="confirmPassword" name="confirmPassword" minlength="6" required class="w-full rounded-lg border border-slate-300 px-3 py-2" type="password">
        </div>
        <button type="submit" class="w-full rounded-lg bg-slate-800 px-4 py-2 text-white hover:bg-slate-700 transition-colors">
            Creer un compte
        </button>
    </form>

    <p class="mt-5 text-sm text-slate-600">
        Deja inscrit?
        <a class="font-semibold text-slate-900 hover:underline" href="${pageContext.request.contextPath}/auth/login">Se connecter</a>
    </p>
</div>
</body>
</html>
