package com.project.pr14;

import com.project.objectes.Llibre;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres.
 */
public class PR14GestioLlibreriaJacksonMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJacksonMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJacksonMain app = new PR14GestioLlibreriaJacksonMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades dels llibres.
     */
    public void processarFitxer() {
        List<Llibre> llibres = carregarLlibres();
        if (llibres != null) {
            modificarAnyPublicacio(llibres, 1, 1995);
            afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
            esborrarLlibre(llibres, 2);
            guardarLlibres(llibres);
        }
    }

    /**
     * Carrega els llibres des del fitxer JSON.
     *
     * @return Llista de llibres o null si hi ha hagut un error en la lectura.
     */
    public List<Llibre> carregarLlibres() {
        // *************** CODI PRÀCTICA **********************/
        ObjectMapper objMapper = new ObjectMapper(); // l'utilitzem per llegir i escriure JSON 
        try {
            return objMapper.readValue(dataFile, new TypeReference<List<Llibre>>() { // llegim el fitxer i el convertim en una llista de llibres
            });
            } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Modifica l'any de publicació d'un llibre amb un id específic.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a modificar.
     * @param nouAny Nou any de publicació.
     */
    public void modificarAnyPublicacio(List<Llibre> llibres, int id, int nouAny) {
        // *************** CODI PRÀCTICA **********************/
        for (Llibre llibre : llibres) { // utilitzem un bucle per recórrer la llista de llibres
            if (llibre.getId() == id) { // si l'id del llibre és igual a l'id que volem modificar fem el canvi
                llibre.setAny(nouAny);
                break; // sortim del bucle un cop hem trobat i modificat el llibre
            }
        }
    }

    /**
     * Afegeix un nou llibre a la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param nouLlibre Nou llibre a afegir.
     */
    public void afegirNouLlibre(List<Llibre> llibres, Llibre nouLlibre) {
        // *************** CODI PRÀCTICA **********************/
        llibres.add(nouLlibre ); // afegim el llibre
    }

    /**
     * Esborra un llibre amb un id específic de la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a esborrar.
     */
    public void esborrarLlibre(List<Llibre> llibres, int id) {
        // *************** CODI PRÀCTICA **********************/
        llibres.removeIf(llibre -> llibre.getId() == id);
    }

    /**
     * Guarda la llista de llibres en un fitxer nou.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        // *************** CODI PRÀCTICA **********************/    
        ObjectMapper objMapper = new ObjectMapper(); 
        try {
            File outputFile = new File(dataFile.getParentFile(), "llibres_output_jackson.json"); // creem el fitxer de sortida
            objMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, llibres); // escrivim la llista de llibres al fitxer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
