package com.dave.dev.jakarta.app.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private LocalDateTime dateCommande;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus statut;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerOrderLine> lignes = new ArrayList<>();

    public CustomerOrder() {
        this.dateCommande = LocalDateTime.now();
        this.montantTotal = BigDecimal.ZERO;
        this.statut = OrderStatus.EN_ATTENTE;
    }

    public void recalculateTotal() {
        this.montantTotal = lignes.stream()
                .map(CustomerOrderLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public OrderStatus getStatut() {
        return statut;
    }

    public void setStatut(OrderStatus statut) {
        this.statut = statut;
    }

    public List<CustomerOrderLine> getLignes() {
        return lignes;
    }

    public void setLignes(List<CustomerOrderLine> lignes) {
        this.lignes = lignes;
    }
}
