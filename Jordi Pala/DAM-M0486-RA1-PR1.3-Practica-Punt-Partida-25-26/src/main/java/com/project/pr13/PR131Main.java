package com.project.pr13;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Classe principal que crea un document XML amb informació de llibres i el guarda en un fitxer.
 * 
 * Aquesta classe permet construir un document XML, afegir elements i guardar-lo en un directori
 * especificat per l'usuari.
 */
public class PR131Main {

    private File dataDir;

    /**
     * Constructor de la classe PR131Main.
     * 
     * @param dataDir Directori on es guardaran els fitxers de sortida.
     */
    public PR131Main(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Retorna el directori de dades actual.
     * 
     * @return Directori de dades.
     */
    public File getDataDir() {
        return dataDir;
    }

    /**
     * Actualitza el directori de dades.
     * 
     * @param dataDir Nou directori de dades.
     */
    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Mètode principal que inicia l'execució del programa.
     * 
     * @param args Arguments passats a la línia de comandament (no s'utilitzen en aquest programa).
     */
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        File dataDir = new File(userDir, "data" + File.separator + "pr13");

        PR131Main app = new PR131Main(dataDir);
        app.processarFitxerXML("biblioteca.xml");
    }

    /**
     * Processa el document XML creant-lo, guardant-lo en un fitxer i comprovant el directori de sortida.
     * 
     * @param filename Nom del fitxer XML a guardar.
     */
    public void processarFitxerXML(String filename) {
        if (comprovarIDirCrearDirectori(dataDir)) {
            Document doc = construirDocument();
            File fitxerSortida = new File(dataDir, filename);
            guardarDocument(doc, fitxerSortida);
        }
    }

    /**
     * Comprova si el directori existeix i, si no és així, el crea.
     * 
     * @param directori Directori a comprovar o crear.
     * @return True si el directori ja existeix o s'ha creat amb èxit, false en cas contrari.
     */
    private boolean comprovarIDirCrearDirectori(File directori) {
        if (!directori.exists()) {
            return directori.mkdirs();
        }
        return true;
    }

    /**
     * Crea un document XML amb l'estructura d'una biblioteca i afegeix un llibre amb els seus detalls.
     * 
     * @return Document XML creat o null en cas d'error.
     */
    private static Document construirDocument() {
        // *************** CODI PRÀCTICA **********************/
       
        try {
            
            DocumentBuilderFactory dbF = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbF.newDocumentBuilder();
            Document doc = db.newDocument();

            // creem l'arrel biblioteca
            Element biblioteca = doc.createElement("bilbioteca");
            doc.appendChild(biblioteca);

            // creem el llibre 001
            Element llibre = doc.createElement("llibre");
            llibre.setAttribute("id", "001");
            biblioteca.appendChild(llibre);

            // afegim el titol 
            Element titol = doc.createElement("titol");
            Text titolText = doc.createTextNode("El viatge dels venturons");
            titol.appendChild(titolText);
            llibre.appendChild(titol);

            // afegim l'autor
            Element autor = doc.createElement("autor");
            Text autorText = doc.createTextNode("Joan Pla");
            autor.appendChild(autorText);
            llibre.appendChild(autor);

            // afegim l'any que s'ha publicat
            Element any = doc.createElement("anyPublicacio");
            Text anyText = doc.createTextNode("1998");
            any.appendChild(anyText);
            llibre.appendChild(any);

            // afegim l'editorial
            Element editorial = doc.createElement("editorial");
            Text editorialText = doc.createTextNode("Edicions Mar");
            editorial.appendChild(editorialText);
            llibre.appendChild(editorial);

            // afegim el genere 
            Element genere = doc.createElement("genere");
            Text genereText = doc.createTextNode("Aventura");
            genere.appendChild(genereText);
            llibre.appendChild(genere);

            // afegim les pagines 
            Element pagines = doc.createElement("pagines");
            Text paginesText = doc.createTextNode("320");
            pagines.appendChild(paginesText);
            llibre.appendChild(pagines);

            // afegim disponibilitat 
            Element disponible = doc.createElement("disponible");
            Text disponibleText = doc.createTextNode("true");
            disponible.appendChild(disponibleText);
            llibre.appendChild(disponible);

            return doc;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * Guarda el document XML proporcionat en el fitxer especificat.
     * 
     * @param doc Document XML a guardar.
     * @param fitxerSortida Fitxer de sortida on es guardarà el document.
     */
    private static void guardarDocument(Document doc, File fitxerSortida) {
        // *************** CODI PRÀCTICA **********************/

        try {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fitxerSortida);

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
