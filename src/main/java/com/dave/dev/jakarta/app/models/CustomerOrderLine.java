package com.dave.dev.jakarta.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ligne_vente")
public class CustomerOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ref_Detail")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RefVente")
    private CustomerOrder order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RefPiece")
    private Piece piece;

    @Column(name = "Qte")
    private Integer quantite;

    @Column(name = "Montant", precision = 12, scale = 2)
    private BigDecimal montant;

    public CustomerOrderLine() {
    }

    public BigDecimal getLineTotal() {
        return montant == null ? BigDecimal.ZERO : montant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
