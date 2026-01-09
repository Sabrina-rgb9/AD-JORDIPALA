package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exemplars")
public class Exemplar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exemplar_id")
    private Long exemplarId;

    @Column(name = "codi_barres", nullable = false, unique = true, length = 50)
    private String codiBarres;

    @Column(nullable = false)
    private boolean disponible = true;  // Valor por defecto

    // relacion m-m amb Llibre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "llibre_id", nullable = false)
    private Llibre llibre;

    // relacion m-m amb Biblioteca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biblioteca_id", nullable = false)
    private Biblioteca biblioteca;

    // relacion 1-m amb Prestec
    @OneToMany(
        mappedBy = "exemplar",           // Campo en Prestec que tiene la FK
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private Set<Prestec> historialPrestecs = new HashSet<>();

    public Exemplar() {}

    public Exemplar(String codiBarres, Llibre llibre, Biblioteca biblioteca) {
        this.codiBarres = codiBarres;
        this.llibre = llibre;
        this.biblioteca = biblioteca;
        this.disponible = true;  // Por defecto disponible
    }

    public Long getExemplarId() { return exemplarId; }
    public void setExemplarId(Long exemplarId) { this.exemplarId = exemplarId; }
    public String getCodiBarres() { return codiBarres; }
    public void setCodiBarres(String codiBarres) { this.codiBarres = codiBarres; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public Llibre getLlibre() { return llibre; }
    public void setLlibre(Llibre llibre) { this.llibre = llibre; }
    public Biblioteca getBiblioteca() { return biblioteca; }
    public void setBiblioteca(Biblioteca biblioteca) { this.biblioteca = biblioteca; }
    public Set<Prestec> getHistorialPrestecs() { return historialPrestecs; }
    public void setHistorialPrestecs(Set<Prestec> historialPrestecs) { 
        this.historialPrestecs = historialPrestecs; 
    }

    // metodos helper para mantener consistencia bidireccional
    public void addPrestec(Prestec prestec) {
        this.historialPrestecs.add(prestec);
        prestec.setExemplar(this); 
    }

    @Override
    public String toString() {
        String titol = (llibre != null) ? llibre.getTitol() : "Desconegut";
        return "Exemplar{id=" + exemplarId + ", codi='" + codiBarres + "', llibre='" + titol + "', disponible=" + disponible + "}";
    }
}