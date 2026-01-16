package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Entitat que representa un autor en el sistema
@Entity
@Table(name = "autors") // nombre de la tabla
public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "autor_id")
    private Long autorId;

    @Column(nullable = false, length = 100)
    private String nom;

    // relacion m-m amb Llibre: un autor puede tener muchos libros y un libro puede tener muchos autores
    @ManyToMany(
        mappedBy = "autors",  // Llibre es el propietario (tiene @JoinTable)
        fetch = FetchType.LAZY
    )
    private Set<Llibre> llibres = new HashSet<>();

    public Autor() {}

    public Autor(String nom) {
        this.nom = nom;
    }

    // Getters y setters
    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Set<Llibre> getLlibres() { return llibres; }
    public void setLlibres(Set<Llibre> llibres) { this.llibres = llibres; }

    // metodos helper para mantener consistencia bidireccional


    public void addLlibre(Llibre llibre) {
        this.llibres.add(llibre);
        llibre.getAutors().add(this); 
    }

    public void removeLlibre(Llibre llibre) {
        this.llibres.remove(llibre);
        llibre.getAutors().remove(this); 
    }

    @Override
    public String toString() {
        return "Autor{id=" + autorId + ", nom='" + nom + "'}";
    }
}