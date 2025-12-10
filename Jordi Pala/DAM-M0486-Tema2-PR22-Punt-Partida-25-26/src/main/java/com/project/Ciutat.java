package com.project;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe que representa una ciutat.
 * 
 * SERIALIZABLE: Permet que l'objecte es pugui convertir a una seqüència de bytes
 * per a persistència, transmissió per xarxa o emmagatzematge en sessió.
 */
public class Ciutat implements Serializable {

    // Identificador únic de la ciutat (clau primària a la base de dades)
    private long ciutatId;
    
    // Nom de la ciutat
    private String nom;
    
    // País al que pertany la ciutat
    private String pais;
    
    // Codi postal (anomenat poblacio en l'enunciat)
    private int poblacio;
    
    /**
     * HASHSET: Col·lecció que emmagatzema els Ciutadans d'aquesta ciutat.
     * - HashSet no permet duplicats (utilitza equals/hashCode per comparar)
     * - No garanteix cap ordre dels elements
     * - Aquesta és la part "One" de la relació OneToMany amb Ciutada
     */
    private Set<Ciutada> ciutadans = new HashSet<>();

    /**
     * CONSTRUCTOR BUIT: Obligatori per Hibernate/JPA.
     * Hibernate necessita crear instàncies buides mitjançant reflexió
     * abans d'omplir els camps amb les dades de la base de dades.
     */
    public Ciutat() {}

    /**
     * Constructor amb paràmetres per facilitar la creació.
     */
    public Ciutat(String nom, String pais, int poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }

    // Getters i setters
    public long getCiutatId() { return ciutatId; }
    public void setCiutatId(long ciutatId) { this.ciutatId = ciutatId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public int getPoblacio() { return poblacio; }
    public void setPoblacio(int poblacio) { this.poblacio = poblacio; }

    public Set<Ciutada> getCiutadans() { return ciutadans; }
    
    /**
     * GESTIÓ BIDIRECCIONAL: Quan afegim un Ciutadà a la ciutat, també hem d'actualitzar
     * la referència inversa (ciutada.setCiutat(this)) per mantenir la coherència
     * entre les dues bandes de la relació.
     */
    public void setCiutadans(Set<Ciutada> ciutadans) {
        this.ciutadans = ciutadans;
        if (this.ciutadans != null) {
            for (Ciutada ciutada : this.ciutadans) {
                ciutada.setCiutat(this);
            }
        }
    }
    
    /**
     * Afegeix un ciutadà a la ciutat mantenint la coherència bidireccional
     */
    public void addCiutada(Ciutada ciutada) {
        if (ciutadans.add(ciutada)) {
            ciutada.setCiutat(this);
        }
    }
    
    /**
     * Elimina un ciutadà de la ciutat mantenint la coherència bidireccional
     */
    public void removeCiutada(Ciutada ciutada) {
        if (ciutadans.remove(ciutada)) {
            ciutada.setCiutat(null);
        }
    }

    /**
     * STRING.FORMAT: Crea una cadena formatada substituint %d per enters
     * i %s per strings. Més llegible que concatenar amb +.
     */
    @Override
    public String toString() {
        String llistaCiutadans = "[]";
        
        if (ciutadans != null && !ciutadans.isEmpty()) {
            llistaCiutadans = ciutadans.stream()
                .map(ciutada -> ciutada.getNom() + " " + ciutada.getCognom())
                .collect(Collectors.joining(", ", "[", "]"));
        }

        return String.format("Ciutat [ID=%d, Nom=%s, Pais=%s, CodiPostal=%d, Ciutadans: %s]", 
                             ciutatId, nom, pais, poblacio, llistaCiutadans);
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
        Ciutat ciutat = (Ciutat) o;
        if (ciutatId == 0 || ciutat.ciutatId == 0) return this == ciutat;
        return ciutatId == ciutat.ciutatId;
    }
    
    /**
     * HASHCODE COHERENT AMB EQUALS:
     * - Si l'entitat té ID (està persistida) → hashCode basat en ID
     * - Si no té ID → hashCode per defecte d'Object (basat en memòria)
     */
    @Override
    public int hashCode() {
        return (ciutatId > 0) ? Objects.hash(ciutatId) : super.hashCode();
    }
}