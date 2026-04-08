package com.dave.dev.jakarta.app.services;

import com.dave.dev.jakarta.app.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardService {

    public DashboardStats loadStats() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            long totalPieces = asLong(em.createNativeQuery("SELECT COUNT(*) FROM Pieces").getSingleResult());
            long totalClients = asLong(em.createNativeQuery("SELECT COUNT(*) FROM Client").getSingleResult());
            long totalVentes = asLong(em.createNativeQuery("SELECT COUNT(*) FROM Ventes").getSingleResult());
            BigDecimal montantTotal = asBigDecimal(
                    em.createNativeQuery("SELECT COALESCE(SUM(Montant), 0) FROM Ventes").getSingleResult()
            );
            return new DashboardStats(totalPieces, totalClients, totalVentes, montantTotal);
        } finally {
            em.close();
        }
    }

    public List<PieceRow> findTopPieces(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> rows = em.createNativeQuery("""
                    SELECT RefPiece, NomPieces, Quantite, P_Vente, Provenance
                    FROM Pieces
                    ORDER BY Quantite DESC
                    LIMIT ?
                    """)
                    .setParameter(1, limit)
                    .getResultList();

            List<PieceRow> pieces = new ArrayList<>();
            for (Object[] row : rows) {
                pieces.add(new PieceRow(
                        asLong(row[0]),
                        asString(row[1]),
                        asInt(row[2]),
                        asBigDecimal(row[3]),
                        asString(row[4])
                ));
            }
            return pieces;
        } finally {
            em.close();
        }
    }

    public List<VenteRow> findRecentVentes(int limit) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> rows = em.createNativeQuery("""
                    SELECT v.RefVente,
                           CONCAT(c.Prenom, ' ', c.Nom) AS clientNom,
                           CONCAT(e.Prenom, ' ', e.Nom) AS employeNom,
                           v.Montant,
                           v.DateVente
                    FROM Ventes v
                    JOIN Client c ON c.RefClient = v.RefClient
                    JOIN Employe e ON e.RefEmploye = v.RefEmploye
                    ORDER BY v.DateVente DESC, v.RefVente DESC
                    LIMIT ?
                    """)
                    .setParameter(1, limit)
                    .getResultList();

            List<VenteRow> ventes = new ArrayList<>();
            for (Object[] row : rows) {
                ventes.add(new VenteRow(
                        asLong(row[0]),
                        asString(row[1]),
                        asString(row[2]),
                        asBigDecimal(row[3]),
                        asLocalDate(row[4])
                ));
            }
            return ventes;
        } finally {
            em.close();
        }
    }

    private long asLong(Object value) {
        return ((Number) value).longValue();
    }

    private int asInt(Object value) {
        return ((Number) value).intValue();
    }

    private BigDecimal asBigDecimal(Object value) {
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return BigDecimal.ZERO;
    }

    private String asString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private LocalDate asLocalDate(Object value) {
        if (value instanceof Date date) {
            return date.toLocalDate();
        }
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        return null;
    }

    public record DashboardStats(long totalPieces, long totalClients, long totalVentes, BigDecimal montantVentes) {
    }

    public record PieceRow(long refPiece, String nomPieces, int quantite, BigDecimal prixVente, String provenance) {
    }

    public record VenteRow(long refVente, String clientNom, String employeNom, BigDecimal montant, LocalDate dateVente) {
    }
}