package com.dave.dev.jakarta.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "pieces")
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RefPiece")
    private Long id;

    @Column(name = "NomPieces", nullable = false, length = 150)
    private String nom;

    @Column(length = 500)
    private String description;

    @Column(name = "P_Achat", precision = 10, scale = 2)
    private BigDecimal prixAchat;

    @Column(name = "P_Vente", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "Quantite")
    private Integer quantite;

    @Column(name = "Provenance", length = 50)
    private String provenance;

    @Column(length = 500)
    private String imageUrl;

    @OneToOne(mappedBy = "piece")
    private Stock stock;

    public Piece() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
