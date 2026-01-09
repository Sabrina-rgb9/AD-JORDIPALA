package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persones")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long personaId;

    @Column(nullable = false, length = 20, unique = true)  
    private String dni;
    
    @Column(nullable = false, length = 100)
    private String nom;
    
    @Column(length = 20)
    private String telefon;
    
    @Column(length = 100)
    private String email;

    // relacion 1-m amb Prestec
    @OneToMany(
        mappedBy = "persona",           // Campo en Prestec que tiene la FK
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private Set<Prestec> prestecs = new HashSet<>();

    public Persona() {}

    public Persona(String dni, String nom, String telefon, String email) {
        this.dni = dni;
        this.nom = nom;
        this.telefon = telefon;
        this.email = email;
    }

    public Long getPersonaId() { return personaId; }
    public void setPersonaId(Long personaId) { this.personaId = personaId; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<Prestec> getPrestecs() { return prestecs; }
    public void setPrestecs(Set<Prestec> prestecs) { this.prestecs = prestecs; }

    // metodo helper para mantener consistencia bidireccional
    public void addPrestec(Prestec prestec) {
        this.prestecs.add(prestec);
        prestec.setPersona(this); 
    }

    @Override
    public String toString() {
        return "Persona{id=" + personaId + ", dni='" + dni + "', nom='" + nom + "'}";
    }
}