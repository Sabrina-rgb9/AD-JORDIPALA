package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "prestecs")
public class Prestec implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prestec_id")
    private Long prestecId;

    // relacion m-1 amb Exemplar (lado propietario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exemplar_id", nullable = false)
    private Exemplar exemplar;

    // relacion m-1 amb Persona (lado propietario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @Column(name = "data_prestec", nullable = false)
    private LocalDate dataPrestec;
    
    @Column(name = "data_retorn_prevista", nullable = false)
    private LocalDate dataRetornPrevista;
    
    @Column(name = "data_retorn_real")
    private LocalDate dataRetornReal;  
    
    @Column(nullable = false)
    private boolean actiu = true;

    public Prestec() {}

    public Prestec(Exemplar exemplar, Persona persona, LocalDate dataPrestec, LocalDate dataRetornPrevista) {
        this.exemplar = exemplar;
        this.persona = persona;
        this.dataPrestec = dataPrestec;
        this.dataRetornPrevista = dataRetornPrevista;
        this.actiu = true; // Por defecto activo
    }

    public Long getPrestecId() { return prestecId; }
    public void setPrestecId(Long prestecId) { this.prestecId = prestecId; }
    public Exemplar getExemplar() { return exemplar; }
    public void setExemplar(Exemplar exemplar) { this.exemplar = exemplar; }
    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }
    public LocalDate getDataPrestec() { return dataPrestec; }
    public void setDataPrestec(LocalDate dataPrestec) { this.dataPrestec = dataPrestec; }
    public LocalDate getDataRetornPrevista() { return dataRetornPrevista; }
    public void setDataRetornPrevista(LocalDate dataRetornPrevista) { 
        this.dataRetornPrevista = dataRetornPrevista; 
    }
    public LocalDate getDataRetornReal() { return dataRetornReal; }
    public void setDataRetornReal(LocalDate dataRetornReal) { 
        this.dataRetornReal = dataRetornReal; 
    }
    public boolean isActiu() { return actiu; }
    public void setActiu(boolean actiu) { this.actiu = actiu; }

    // modificado toString para evitar problemas con LAZY loading
    @Override
    public String toString() {
        String nomPersona;
        String codiExemplar;
        
        // Manejar proxy LAZY para persona
        try {
            nomPersona = (persona != null) ? persona.getNom() : "Desc.";
        } catch (org.hibernate.LazyInitializationException e) {
            nomPersona = "[Persona no carregada]";
        }
        
        // Manejar proxy LAZY para exemplar
        try {
            codiExemplar = (exemplar != null) ? exemplar.getCodiBarres() : "Desc.";
        } catch (org.hibernate.LazyInitializationException e) {
            codiExemplar = "[Exemplar no carregat]";
        }
        
        return "Prestec{id=" + prestecId + 
            ", exemplar=" + codiExemplar + 
            ", persona=" + nomPersona + 
            ", data=" + dataPrestec + 
            ", actiu=" + actiu + "}";
    }
}