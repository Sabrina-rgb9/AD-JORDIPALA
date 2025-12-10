package com.project;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe que representa un ciutadà.
 * 
 * SERIALIZABLE: Permet que l'objecte es pugui convertir a una seqüència de bytes
 * per a persistència, transmissió per xarxa o emmagatzematge en sessió.
 */
public class Ciutada implements Serializable {

    // Identificador únic del ciutadà (clau primària a la base de dades)
    private long ciutadaId;
    
    // Nom del ciutadà
    private String nom;
    
    // Cognom del ciutadà
    private String cognom;
    
    // Edat del ciutadà
    private int edat;
    
    /**
     * RELACIÓ MANYTOONE: Referència a la ciutat on viu aquest ciutadà.
     * Aquesta és la part "Many" de la relació (molts Ciutadans pertanyen a una Ciutat).
     * És el costat INVERS de la relació bidireccional amb Ciutat.
     */
    private Ciutat ciutat;

    /**
     * CONSTRUCTOR BUIT: Obligatori per Hibernate/JPA.
     * Hibernate necessita crear instàncies buides mitjançant reflexió
     * abans d'omplir els camps amb les dades de la base de dades.
     */
    public Ciutada() {}

    /**
     * Constructor amb paràmetres per facilitar la creació.
     */
    public Ciutada(String nom, String cognom, int edat) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
    }

    // Getters i setters
    public long getCiutadaId() { return ciutadaId; }
    public void setCiutadaId(long ciutadaId) { this.ciutadaId = ciutadaId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognom() { return cognom; }
    public void setCognom(String cognom) { this.cognom = cognom; }

    public int getEdat() { return edat; }
    public void setEdat(int edat) { this.edat = edat; }

    public Ciutat getCiutat() { return ciutat; }
    public void setCiutat(Ciutat ciutat) { this.ciutat = ciutat; }

    /**
     * STRING.FORMAT: Crea una cadena formatada substituint %d per enters
     * i %s per strings. Més llegible que concatenar amb +.
     */
    @Override
    public String toString() {
    return String.format("Ciutada [ID=%d, Nom=%s, Cognom=%s, Edat=%d]", 
                         ciutadaId, nom, cognom, edat);
    }

    /**
     * EQUALS PER ENTITATS JPA:
     * - Si és el mateix objecte en memòria → true
     * - Si algun dels dos no està persistit (ID=0) → compara per referència
     * - Si tots dos estan persistits → compara per ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ciutada ciutada = (Ciutada) o;
        if (ciutadaId == 0 || ciutada.ciutadaId == 0) return this == ciutada;
        return ciutadaId == ciutada.ciutadaId;
    }
    
    /**
     * HASHCODE COHERENT AMB EQUALS:
     * - Si l'entitat té ID (està persistida) → hashCode basat en ID
     * - Si no té ID → hashCode per defecte d'Object (basat en memòria)
     */
    @Override
    public int hashCode() {
        return (ciutadaId > 0) ? Objects.hash(ciutadaId) : super.hashCode();
    }
}