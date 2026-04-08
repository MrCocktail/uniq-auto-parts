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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tailwind.css">
</head>
<body class="min-h-screen bg-slate-100">
<%
    Employee currentUser = (Employee) request.getAttribute("currentUser");
    DashboardService.DashboardStats stats = (DashboardService.DashboardStats) request.getAttribute("stats");
    List<DashboardService.PieceRow> pieces = (List<DashboardService.PieceRow>) request.getAttribute("pieces");
    List<DashboardService.VenteRow> ventes = (List<DashboardService.VenteRow>) request.getAttribute("ventes");
    String dashboardError = (String) request.getAttribute("dashboardError");
%>
<header class="bg-slate-900 text-white">
    <div class="mx-auto max-w-6xl px-6 py-4 flex items-center justify-between">
        <h1 class="text-xl font-semibold">Uniq Auto Parts beeeeee</h1>
        <div class="text-sm">
            <span class="mr-4">Connecte: <strong><%= currentUser != null ? currentUser.getUsername() : "-" %></strong></span>
            <a class="underline" href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        </div>
    </div>
</header>

<main class="mx-auto max-w-6xl px-6 py-10">
    <% if (dashboardError != null) { %>
    <div class="mb-6 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
        <%= dashboardError %>
    </div>
    <% } %>

    <section class="grid gap-5 md:grid-cols-3">
        <div class="rounded-xl bg-white p-6 shadow">
            <p class="text-sm text-slate-500">Pieces en stock</p>
            <h2 class="mt-2 text-2xl font-bold text-slate-900"><%= stats != null ? stats.totalPieces() : 0 %></h2>
        </div>
        <div class="rounded-xl bg-white p-6 shadow">
            <p class="text-sm text-slate-500">Clients</p>
            <h2 class="mt-2 text-2xl font-bold text-slate-900"><%= stats != null ? stats.totalClients() : 0 %></h2>
        </div>
        <div class="rounded-xl bg-white p-6 shadow">
            <p class="text-sm text-slate-500">Montant total ventes</p>
            <h2 class="mt-2 text-2xl font-bold text-slate-900"><%= stats != null ? stats.montantVentes() : 0 %> HTG</h2>
            <p class="text-xs text-slate-500 mt-1"><%= stats != null ? stats.totalVentes() : 0 %> vente(s)</p>
        </div>
    </section>

    <section class="mt-8 grid gap-5 md:grid-cols-2">
        <div class="rounded-xl bg-white p-6 shadow">
            <h3 class="text-lg font-semibold text-slate-900">Top pieces (quantite)</h3>
            <div class="mt-4 overflow-x-auto">
                <table class="w-full text-sm">
                    <thead>
                    <tr class="text-left text-slate-500">
                        <th class="py-2">Ref</th>
                        <th class="py-2">Piece</th>
                        <th class="py-2">Qt</th>
                        <th class="py-2">Prix</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (pieces != null && !pieces.isEmpty()) { %>
                    <% for (DashboardService.PieceRow piece : pieces) { %>
                    <tr class="border-t border-slate-200 text-slate-700">
                        <td class="py-2"><%= piece.refPiece() %></td>
                        <td class="py-2"><%= piece.nomPieces() %></td>
                        <td class="py-2"><%= piece.quantite() %></td>
                        <td class="py-2"><%= piece.prixVente() %> HTG</td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="4" class="py-3 text-slate-500">Aucune piece trouvee.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="rounded-xl bg-white p-6 shadow">
            <h3 class="text-lg font-semibold text-slate-900">Ventes recentes</h3>
            <div class="mt-4 overflow-x-auto">
                <table class="w-full text-sm">
                    <thead>
                    <tr class="text-left text-slate-500">
                        <th class="py-2">Ref</th>
                        <th class="py-2">Client</th>
                        <th class="py-2">Employe</th>
                        <th class="py-2">Montant</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (ventes != null && !ventes.isEmpty()) { %>
                    <% for (DashboardService.VenteRow vente : ventes) { %>
                    <tr class="border-t border-slate-200 text-slate-700">
                        <td class="py-2"><%= vente.refVente() %></td>
                        <td class="py-2"><%= vente.clientNom() %></td>
                        <td class="py-2"><%= vente.employeNom() %></td>
                        <td class="py-2"><%= vente.montant() %> HTG</td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="4" class="py-3 text-slate-500">Aucune vente trouvee.</td>
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
