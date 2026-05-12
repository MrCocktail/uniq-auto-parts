<%@ page import="com.dave.dev.jakarta.app.models.Employee" %>
<%@ page import="com.dave.dev.jakarta.app.services.DashboardService" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Uniq Auto Parts</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-50 text-slate-800" style="font-family: 'Outfit', sans-serif; background-image: radial-gradient(circle at 12% 8%, rgba(59, 130, 246, 0.18), transparent 32%), radial-gradient(circle at 88% 14%, rgba(14, 165, 233, 0.16), transparent 28%), radial-gradient(circle at 50% 100%, rgba(16, 185, 129, 0.12), transparent 35%);">
<%
    Employee currentUser = (Employee) request.getAttribute("currentUser");
    DashboardService.DashboardStats stats = (DashboardService.DashboardStats) request.getAttribute("stats");
    List<DashboardService.PieceRow> pieces = (List<DashboardService.PieceRow>) request.getAttribute("pieces");
    List<DashboardService.VenteRow> ventes = (List<DashboardService.VenteRow>) request.getAttribute("ventes");
    String dashboardError = (String) request.getAttribute("dashboardError");
%>
<div class="pointer-events-none fixed inset-x-0 top-0 -z-10 h-72 bg-gradient-to-b from-white/90 via-slate-100/70 to-transparent"></div>

<header class="border-b border-slate-200/80 bg-white/90 backdrop-blur">
    <div class="mx-auto flex max-w-7xl flex-wrap items-center justify-between gap-3 px-6 py-4">
        <div>
            <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">Dashboard</p>
            <h1 class="text-2xl font-bold text-slate-900">Uniq Auto Parts</h1>
        </div>
        <nav class="flex flex-wrap items-center gap-3 text-sm font-semibold text-slate-600">
            <a class="text-slate-900" href="${pageContext.request.contextPath}/home">Dashboard</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/pieces">Pieces</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/clients">Clients</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/orders">Commandes</a>
            <a class="hover:text-slate-900" href="${pageContext.request.contextPath}/stock">Stock</a>
        </nav>
        <div class="flex items-center gap-3 rounded-xl border border-slate-200 bg-white px-3 py-2 text-sm">
            <span class="inline-flex h-8 w-8 items-center justify-center rounded-full bg-blue-100 text-xs font-bold text-blue-700">
                <%= currentUser != null && currentUser.getEmail() != null && !currentUser.getEmail().isEmpty() ? currentUser.getEmail().substring(0, 1).toUpperCase() : "U" %>
            </span>
            <div>
                <!-- <p class="text-xs text-slate-500">Connecte</p> -->
                <p class="font-semibold text-slate-800"><%= currentUser != null ? currentUser.getEmail() : "-" %></p>
            </div>
            <a class="ml-2 rounded-lg border border-slate-200 px-3 py-1.5 font-medium text-slate-600 transition hover:bg-red-100" href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </div>
    </div>
</header>

<main class="mx-auto max-w-7xl px-6 py-8 md:py-10">
    <% if (dashboardError != null) { %>
    <div class="mb-6 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-red-700 shadow-sm">
        <%= dashboardError %>
    </div>
    <% } %>

    <section class="grid grid-cols-1 gap-4 text-center sm:grid-cols-2 md:grid-cols-3">
        <div class="flex flex-col justify-center rounded-lg bg-blue-100 p-6 shadow-sm">
            <div class="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-blue-200 text-blue-700">
                <svg class="h-8 w-8" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7L12 3 4 7m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                </svg>
            </div>
            <p class="mt-4 text-xs font-bold uppercase tracking-wider text-blue-600">Pieces en stock</p>
            <h2 class="mt-2 text-4xl font-extrabold text-blue-800"><%= stats != null ? stats.totalPieces() : 0 %></h2>
        </div>

        <div class="flex flex-col justify-center rounded-lg bg-fuchsia-100 p-6 shadow-sm">
            <div class="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-fuchsia-200 text-fuchsia-700">
                <svg class="h-8 w-8" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20a4 4 0 00-8 0m8 0H5m12 0h2M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 13a4 4 0 00-5-3.87M16 3.13a3 3 0 010 5.75"></path>
                </svg>
            </div>
            <p class="mt-4 text-xs font-bold uppercase tracking-wider text-fuchsia-600">Clients</p>
            <h2 class="mt-2 text-4xl font-extrabold text-fuchsia-800"><%= stats != null ? stats.totalClients() : 0 %></h2>
        </div>

        <div class="flex flex-col justify-center rounded-lg bg-emerald-100 p-6 shadow-sm">
            <div class="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-emerald-200 text-emerald-700">
                <svg class="h-8 w-8" fill="none" stroke="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-2.21 0-4 1.12-4 2.5S9.79 13 12 13s4 1.12 4 2.5S14.21 18 12 18m0-10v10m0-10V6m0 12v2"></path>
                </svg>
            </div>
            <p class="mt-4 text-xs font-bold uppercase tracking-wider text-emerald-600">Montant total ventes</p>
            <h2 class="mt-2 text-4xl font-extrabold text-emerald-800"><%= stats != null ? String.format("%,.2f", stats.montantVentes()) : "0.00" %> <span class="text-xl font-bold">HTG</span></h2>
            <p class="mt-1 text-xs text-slate-600"><%= stats != null ? stats.totalVentes() : 0 %> vente(s) enregistree(s)</p>
        </div>
    </section>

    <section class="mt-8 grid gap-5 xl:grid-cols-2">
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
            <div class="mb-4 flex items-center justify-between">
                <h3 class="text-lg font-bold text-slate-900">Top pieces</h3>
                <span class="rounded-md bg-slate-100 px-2.5 py-1 text-xs font-semibold text-slate-600">Stock</span>
            </div>
            <div class="mt-4 overflow-x-auto">
                <table class="w-full table-fixed text-sm">
                    <colgroup>
                        <col style="width: 12%;">
                        <col style="width: 48%;">
                        <col style="width: 14%;">
                        <col style="width: 26%;">
                    </colgroup>
                    <thead>
                    <tr class="border-b border-slate-100 text-left text-xs uppercase tracking-wide text-slate-500">
                        <th class="py-3 font-semibold">Ref</th>
                        <th class="py-3 font-semibold">Piece</th>
                        <th class="py-3 font-semibold">Qt</th>
                        <th class="py-3 font-semibold">Prix</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (pieces != null && !pieces.isEmpty()) { %>
                    <% for (DashboardService.PieceRow piece : pieces) { %>
                    <tr class="border-b border-slate-100 text-slate-700 last:border-b-0">
                        <td class="py-3 font-semibold"><%= piece.refPiece() %></td>
                        <td class="py-3" style="word-break: break-word;"><%= piece.nomPieces() %></td>
                        <td class="py-3"><span class="rounded-md bg-slate-100 px-2 py-1 text-xs font-semibold text-slate-700"><%= piece.quantite() %></span></td>
                        <td class="py-3 font-semibold"><%= piece.prixVente() %> HTG</td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="4" class="py-4 text-slate-500">Aucune piece trouvee.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
            <div class="mb-4 flex items-center justify-between">
                <h3 class="text-lg font-bold text-slate-900">Ventes recentes</h3>
                <span class="rounded-md bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700"><%= stats != null ? stats.totalVentes() : 0 %> total</span>
            </div>
            <div class="mt-4 overflow-x-auto">
                <table class="w-full table-fixed text-sm">
                    <colgroup>
                        <col style="width: 12%;">
                        <col style="width: 31%;">
                        <col style="width: 31%;">
                        <col style="width: 26%;">
                    </colgroup>
                    <thead>
                    <tr class="border-b border-slate-100 text-left text-xs uppercase tracking-wide text-slate-500">
                        <th class="py-3 font-semibold">Ref</th>
                        <th class="py-3 font-semibold">Client</th>
                        <th class="py-3 font-semibold">Employe</th>
                        <th class="py-3 font-semibold">Montant</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (ventes != null && !ventes.isEmpty()) { %>
                    <% for (DashboardService.VenteRow vente : ventes) { %>
                    <tr class="border-b border-slate-100 text-slate-700 last:border-b-0">
                        <td class="py-3 font-semibold"><%= vente.refVente() %></td>
                        <td class="py-3" style="word-break: break-word;"><%= vente.clientNom() %></td>
                        <td class="py-3" style="word-break: break-word;"><%= vente.employeNom() %></td>
                        <td class="py-3 font-semibold text-emerald-700"><%= vente.montant() %> HTG</td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="4" class="py-4 text-slate-500">Aucune vente trouvee.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</main>
</body>
</html>
