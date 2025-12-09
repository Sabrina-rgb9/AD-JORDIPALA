package com.project;

import java.io.Serializable;

public class Ciutada implements Serializable {

    private long ciutadaId;
    private String nom;
    private String cognom;
    private int edat;

    // Relació *lògica*, no física
    private long ciutatId; // FK lògica

    public Ciutada() {}

    public Ciutada(String nom, String cognom, int edat) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
    }

    public long getCiutadaId() { return ciutadaId; }
    public void setCiutadaId(long ciutadaId) { this.ciutadaId = ciutadaId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognom() { return cognom; }
    public void setCognom(String cognom) { this.cognom = cognom; }

    public int getEdat() { return edat; }
    public void setEdat(int edat) { this.edat = edat; }

    public long getCiutatId() { return ciutatId; }
    public void setCiutatId(long ciutatId) { this.ciutatId = ciutatId; }

    @Override
    public String toString() {
        return String.format("Ciutada [ID=%d, Nom=%s, Cognom=%s, Edat=%d, CiutatID=%d]",
                ciutadaId, nom, cognom, edat, ciutatId);
    }
}
