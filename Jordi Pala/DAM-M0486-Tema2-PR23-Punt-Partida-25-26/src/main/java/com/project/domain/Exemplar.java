package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Entitat que representa un exemplar d'un llibre en una biblioteca
@Entity 
@Table(name = "exemplars") // nombre de la tabla
public class Exemplar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exemplar_id")
    private Long exemplarId;

    @Column(name = "codi_barres", nullable = false, unique = true, length = 50)
    private String codiBarres; // Código de barras único para cada ejemplar

    @Column(nullable = false)
    private boolean disponible = true;  // Valor por defecto disponible 

    // relacion m-0 con libro: un exemplar pertenece a un solo libro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "llibre_id", nullable = false) // FK a Llibre
    private Llibre llibre;

    // relacion m-0 con biblioteca: un exemplar pertenece a una sola biblioteca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biblioteca_id", nullable = false) // FK a Biblioteca
    private Biblioteca biblioteca;

    // relacion 1-m amb Prestec: un exemplar puede tener muchos prestamos en su historial
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

    // metodo para añadir un prestec al historial
    public void addPrestec(Prestec prestec) {
        this.historialPrestecs.add(prestec);
        prestec.setExemplar(this); 
    }

    @Override
    public String toString() {
        // Solución segura
        String titol = "Desconegut";
        if (llibre != null) {
            // Verificar si es un proxy Hibernate
            try {
                titol = llibre.getTitol();
            } catch (org.hibernate.LazyInitializationException e) {
                titol = "[Títol no disponible]";
            }
        }
        return "Exemplar{id=" + exemplarId + ", codi='" + codiBarres + "', disponible=" + disponible + "}";
    }
}