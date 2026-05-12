# TODO Implementation - Uniq Auto Parts

## Decisions
- Build Tailwind en local (npm).
- Login obligatoire (session + password hash).
- Les images des pieces sont des URLs distantes stockees en base.

## Phase 1 - Foundation
- [x] Ajouter dependencies Maven (Hibernate, MySQL, BCrypt, logging, tests).
- [x] Creer `persistence.xml` pour MySQL.
- [x] Creer les entites de base: Piece, Stock, Client, CustomerOrder, CustomerOrderLine, Employee.
- [x] Ajouter `JPAUtil`.

## Phase 2 - Auth Vertical Slice
- [x] Creer `AuthService` (signup/login).
- [x] Creer `EmployeeDAO`.
- [x] Creer `AuthServlet` (login/signup/logout).
- [x] Creer `AuthFilter` pour proteger les routes.
- [x] Creer JSPs `login.jsp`, `signup.jsp`, `home.jsp`.

## Phase 3 - Tailwind Local
- [x] Ajouter `package.json`.
- [x] Ajouter `tailwind.config.js`.
- [x] Ajouter `src/main/webapp/css/input.css`.
- [x] Lancer `npm install`.
- [x] Lancer `npm run tailwind:build`.

## Phase 4 - Business Features (Next)
- [x] Implementer CRUD Piece (DAO + Service + Servlet + JSPs list/create/edit).
- [x] Implementer gestion Stock (ajustement + validation quantites).
- [x] Implementer CRUD Client.
- [x] Implementer creation CommandeClient avec lignes et calcul montant.
- [x] Implementer update statut commande.

## Phase 5 - Quality
- [x] Ajouter validations serveur detaillees (prix >= 0, email valide, reference unique).
- [x] Ajouter gestion erreurs centralisee.
- [x] Ajouter tests unitaires services critiques.
- [x] Ajouter donnees seed admin initial pour premier login.

## Quick Run
1. Configurer MySQL et creer DB `autoparts`.
2. Demarrer avec `mvnw clean package wildfly:dev`.
3. Ouvrir `/auth/signup` pour creer le premier employe.
4. Construire le CSS: `npm install` puis `npm run tailwind:build`.
