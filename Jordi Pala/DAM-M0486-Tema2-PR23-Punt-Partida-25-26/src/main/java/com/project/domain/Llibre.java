package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Entitat que representa un llibre en el sistema
@Entity
@Table(name = "llibres") // nombre de la tabla
public class Llibre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "llibre_id")
    private Long llibreId;

    @Column(nullable = false, length = 20, unique = true) 
    private String isbn;
    
    @Column(nullable = false, length = 255)
    private String titol;
    
    @Column(length = 100)
    private String editorial;
    
    @Column(name = "any_publicacio")
    private Integer anyPublicacio;

    // relacion muchos a muchos con autor: un autor puede tener muchos libros y un libro puede tener muchos auto
    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "llibres_autors",                     // Tabla intermedia
        joinColumns = @JoinColumn(name = "llibre_id"),       // FK a esta entidad
        inverseJoinColumns = @JoinColumn(name = "autor_id")  // FK a la otra entidad (se necesita como tabla intermedia)
    )
    private Set<Autor> autors = new HashSet<>();

    @OneToMany(
        mappedBy = "llibre",         // Campo en Exemplar que tiene la FK
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private Set<Exemplar> exemplars = new HashSet<>();

    public Llibre() {}

    public Llibre(String isbn, String titol, String editorial, Integer anyPublicacio) {
        this.isbn = isbn;
        this.titol = titol;
        this.editorial = editorial;
        this.anyPublicacio = anyPublicacio;
    }

    // Getters y setters
    public Long getLlibreId() { return llibreId; }
    public void setLlibreId(Long llibreId) { this.llibreId = llibreId; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitol() { return titol; }
    public void setTitol(String titol) { this.titol = titol; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public Integer getAnyPublicacio() { return anyPublicacio; }
    public void setAnyPublicacio(Integer anyPublicacio) { this.anyPublicacio = anyPublicacio; }
    public Set<Autor> getAutors() { return autors; }
    public void setAutors(Set<Autor> autors) { this.autors = autors; }
    public Set<Exemplar> getExemplars() { return exemplars; }
    public void setExemplars(Set<Exemplar> exemplars) { this.exemplars = exemplars; }

    // metodos helper para mantener consistencia bidireccional

    // addAutor sirve para añadir un autor al llibre y actualizar el lado inverso (el lado inverso es el que no tiene la @JoinTable)
    public void addAutor(Autor autor) {
        this.autors.add(autor);
        autor.getLlibres().add(this);  // Actualizar lado inverso
    }

    // removeAutor sirve para eliminar un autor del llibre y actualizar el lado inverso
    public void removeAutor(Autor autor) {
        this.autors.remove(autor);
        autor.getLlibres().remove(this);  // Actualizar lado inverso
    }

    // addExemplar sirve para añadir un exemplar al llibre y actualizar el lado propietario (el lado propietario es el que tiene la FK)
    public void addExemplar(Exemplar exemplar) {
        this.exemplars.add(exemplar);
        exemplar.setLlibre(this);  // Actualizar lado propietario
    }

    // removeExemplar sirve para eliminar un exemplar del llibre y actualizar el lado propietario
    public void removeExemplar(Exemplar exemplar) {
        this.exemplars.remove(exemplar);
        exemplar.setLlibre(null);
    }

    @Override
    public String toString() {
        return "Llibre{id=" + llibreId + ", isbn='" + isbn + "', titol='" + titol + "'}";
    }
}