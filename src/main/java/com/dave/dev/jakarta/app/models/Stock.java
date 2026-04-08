package com.dave.dev.jakarta.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "piece_id", nullable = false, unique = true)
    private Piece piece;

    @Column(nullable = false)
    private Integer quantiteDisponible;

    @Column(nullable = false)
    private Integer quantiteReservee;

    @Column(nullable = false)
    private LocalDateTime dateMaj;

    public Stock() {
        this.quantiteDisponible = 0;
        this.quantiteReservee = 0;
        this.dateMaj = LocalDateTime.now();
    }

    public int quantiteLibre() {
        return quantiteDisponible - quantiteReservee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Integer getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public void setQuantiteDisponible(Integer quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public Integer getQuantiteReservee() {
        return quantiteReservee;
    }

    public void setQuantiteReservee(Integer quantiteReservee) {
        this.quantiteReservee = quantiteReservee;
    }

    public LocalDateTime getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(LocalDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }
}
