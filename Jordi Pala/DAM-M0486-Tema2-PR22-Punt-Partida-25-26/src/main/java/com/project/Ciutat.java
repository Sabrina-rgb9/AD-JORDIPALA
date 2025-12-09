package com.project;

import java.io.Serializable;

public class Ciutat implements Serializable {

    private long ciutatId;
    private String nom;
    private String pais;
    private int poblacio; // codi postal

    public Ciutat() {}

    public Ciutat(String nom, String pais, int poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }

    public long getCiutatId() { return ciutatId; }
    public void setCiutatId(long ciutatId) { this.ciutatId = ciutatId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public int getPoblacio() { return poblacio; }
    public void setPoblacio(int poblacio) { this.poblacio = poblacio; }

    @Override
    public String toString() {
        return String.format("Ciutat [ID=%d, Nom=%s, Pais=%s, Poblacio=%d]",
                ciutatId, nom, pais, poblacio);
    }
}
