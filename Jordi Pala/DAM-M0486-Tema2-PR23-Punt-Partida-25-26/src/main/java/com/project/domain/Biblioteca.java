package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Entitat que representa una biblioteca en el sistema
@Entity // Taula "biblioteques" a la base de dades
@Table(name = "biblioteques") // nombre de la tabla
public class Biblioteca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biblioteca_id")
    private Long bibliotecaId;

    @Column(nullable = false, length = 200)
    private String nom;
    
    @Column(nullable = false, length = 100)
    private String ciutat;
    
    @Column(length = 255)
    private String adreca;
    
    @Column(length = 20)
    private String telefon;
    
    @Column(length = 100)
    private String email;

    // relació amb Exemplar: una biblioteca pot tenir molts exemplars
    @OneToMany(mappedBy = "biblioteca")
    
    private Set<Exemplar> exemplars = new HashSet<>();

    public Biblioteca() {}

    public Biblioteca(String nom, String ciutat, String adreca, String telefon, String email) {
        this.nom = nom;
        this.ciutat = ciutat;
        this.adreca = adreca;
        this.telefon = telefon;
        this.email = email;
    }

    public Long getBibliotecaId() { return bibliotecaId; }
    public void setBibliotecaId(Long bibliotecaId) { this.bibliotecaId = bibliotecaId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCiutat() { return ciutat; }
    public void setCiutat(String ciutat) { this.ciutat = ciutat; }
    public String getAdreca() { return adreca; }
    public void setAdreca(String adreca) { this.adreca = adreca; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<Exemplar> getExemplars() { return exemplars; }
    public void setExemplars(Set<Exemplar> exemplars) { this.exemplars = exemplars; }

    @Override
    public String toString() {
        return "Biblioteca{id=" + bibliotecaId + ", nom='" + nom + "', ciutat='" + ciutat + "'}";
    }
}