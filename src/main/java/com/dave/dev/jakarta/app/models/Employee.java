package com.dave.dev.jakarta.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "employe")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RefEmploye")
    private Long refEmploye;

    @Column(name = "Prenom")
    private String prenom;

    @Column(name = "Nom")
    private String nom;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Salaire")
    private BigDecimal salaire;

    @Column(name = "Email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "MotDePasseHash", nullable = false, length = 255)
    private String passwordHash;

    public Employee() {
    }

    public Long getRefEmploye() {
        return refEmploye;
    }

    public void setRefEmploye(Long refEmploye) {
        this.refEmploye = refEmploye;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getSalaire() {
        return salaire;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
