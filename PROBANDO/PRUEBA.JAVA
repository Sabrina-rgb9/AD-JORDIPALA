package cat.iesesteveterradas.fites;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

/**
 * Implementa el codi que realitzi el següent:
 * 
 * - Genera documents XML i JSON a partir de la informació sobre llenguatges de
 * programació continguda en una llista.
 * - Guarda l'estructura XML i JSON generada en fitxers a les rutes
 * especificades amb els noms 'Exercici3.xml' i 'Exercici3.json'.
 * - Per crear el fitxer JSON cal usar Jakarta.
 * - Gestió d'errors: si hi ha algun problema en escriure o generar els fitxers,
 * mostra l'excepció a la consola.
 * 
 * Documents esperats (el contingut és important, el format bonic no es te en
 * compte):
 * 
 * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
 * <llista>
 * <llenguatge dificultat="alta" extensio=".c">
 * <nom>C</nom>
 * <any>1972</any>
 * </llenguatge>
 * <llenguatge dificultat="mitjana" extensio=".java">
 * <nom>Java</nom>
 * <any>1995</any>
 * </llenguatge>
 * <llenguatge dificultat="baixa" extensio=".js">
 * <nom>Javascript</nom>
 * <any>1995</any>
 * </llenguatge>
 * <llenguatge dificultat="baixa" extensio=".m">
 * <nom>Objective C</nom>
 * <any>1984</any>
 * </llenguatge>
 * <llenguatge dificultat="mitjana" extensio=".dart">
 * <nom>Dart</nom>
 * <any>2011</any>
 * </llenguatge>
 * </llista>
 * 
 * 
 * [
 * {
 * "nom": "C",
 * "any": "1972",
 * "extensio": ".c",
 * "dificultat": "alta"
 * },
 * {
 * "nom": "Java",
 * "any": "1995",
 * "extensio": ".java",
 * "dificultat": "mitjana"
 * },
 * {
 * "nom": "Javascript",
 * "any": "1995",
 * "extensio": ".js",
 * "dificultat": "baixa"
 * },
 * {
 * "nom": "Objective C",
 * "any": "1984",
 * "extensio": ".m",
 * "dificultat": "baixa"
 * },
 * {
 * "nom": "Dart",
 * "any": "2011",
 * "extensio": ".dart",
 * "dificultat": "mitjana"
 * }
 * ]
 */

public class Exercici3 {
    private String filePathXML;
    private String filePathJson;

    public static void main(String[] args) {
        String basePath = System.getProperty("user.dir") + "/data/exercici3/";
        String filePathXML = basePath + "Exercici3.xml";
        String filePathJson = basePath + "Exercici3.json";

        Exercici3 exercici = new Exercici3();
        exercici.configuraPathFitxerSortidaXML(filePathXML);
        exercici.configuraPathFitxerSortidaJson(filePathJson);
        exercici.executa();
    }

    // Mètode que executa la lògica de la classe
    public void executa() {
        ArrayList<String[]> llista = new ArrayList<>();
        llista.add(new String[] { "C", "1972", ".c", "alta" });
        llista.add(new String[] { "Java", "1995", ".java", "mitjana" });
        llista.add(new String[] { "Javascript", "1995", ".js", "baixa" });
        llista.add(new String[] { "Objective C", "1984", ".m", "baixa" });
        llista.add(new String[] { "Dart", "2011", ".dart", "mitjana" });

        // Crear i guardar el document XML
        Document doc = crearDocumentXML(llista);
        guardarXML(filePathXML, doc);

        // Crear i guardar el document JSON
        guardarJSON(filePathJson, llista);
    }

    // Mètode que crea el document XML a partir de la llista proporcionada
    private Document crearDocumentXML(ArrayList<String[]> llista) {
        // *************** CODI EXERCICI FITA **********************/
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document doc = dbBuilder.newDocument();

            Element rootElement = doc.createElement("llista"); // Crea l'element arrel
            doc.appendChild(rootElement); // Afegeix l'element arrel al document

            for (String[] llenguatge : llista) {
                Element llenguatgeElement = doc.createElement("llenguatge"); // Crea l'element llenguatge
                llenguatgeElement.setAttribute("dificultat", llenguatge[3]); // Afegir atribut dificultat a partir del
                                                                             // index 3 de la llista
                llenguatgeElement.setAttribute("extensio", llenguatge[2]); // Afegir atribut extensio a partir del index
                                                                           // 2 de la llista

                Element nomElement = doc.createElement("nom"); // Crea l'element nom
                nomElement.appendChild(doc.createTextNode(llenguatge[0])); // Introduir el nom del llenguatge de la
                                                                           // llista
                llenguatgeElement.appendChild(nomElement); // Afegir a l'element llenguatge l'element nom

                Element anyElement = doc.createElement("any"); // Crea l'element any
                anyElement.appendChild(doc.createTextNode(llenguatge[1])); // Introduir l'any del llenguatge de l'index
                                                                           // 1
                                                                           // de la llista
                llenguatgeElement.appendChild(anyElement); // Afegir l'element any a l'element llenguatge

                rootElement.appendChild(llenguatgeElement); // Finalment, afegir l'element llenguatge a l'arrel
            }

            return doc; // Retorna el document creat

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Escriu un Document en un fitxer XML
    public static void guardarXML(String path, Document doc) {
        // *************** CODI EXERCICI FITA **********************/
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File(path));

            transformer.transform(source, result);

        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    // Escriu la llista en un fitxer JSON utilitzant Jakarta JSON-B
    public void guardarJSON(String path, ArrayList<String[]> llista) {
        // *************** CODI EXERCICI FITA **********************/
        try {
            // Convertir cada String[] en un Map per serialitzar com a objecte JSON
            List<Map<String, String>> listaObjects = new ArrayList<>();

            // Omplir la llista de mapes
            for (String[] llenguatge : llista) {
                Map<String, String> obj = new LinkedHashMap<>();
                obj.put("nom", llenguatge[0]);
                obj.put("any", llenguatge[1]);
                obj.put("extensio", llenguatge[2]);
                obj.put("dificultat", llenguatge[3]);
                listaObjects.add(obj); // Afegir el map a la llista
            }

            // Escriure a fitxer usant Jakarta JSON-B
            File outputFile = new File(path);
            outputFile.getParentFile().mkdirs();

            try (FileWriter fiw = new FileWriter(outputFile)) {
                Jsonb jsonb = JsonbBuilder.create();
                JsonbConfig config = new JsonbConfig().withFormatting(true); // Configuració per formatar el JSON
                jsonb = JsonbBuilder.create(config);
                jsonb.toJson(listaObjects, fiw);
            }

        } catch (IOException e) {
            System.err.println("Error en guardar el fitxer JSON: " + e.getMessage());
        }
    }

    /****************************************************************************/
    /* NO CAL TOCAR */
    /****************************************************************************/
    // Retorna els nodes d'una expressió XPath
    public static NodeList getNodeList(Document doc, String expression) {
        NodeList llista = null;
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            llista = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return llista;
    }

    // Setter per definir el path XML
    public void configuraPathFitxerSortidaXML(String filePath) {
        this.filePathXML = filePath;
    }

    // Setter per definir el path JSON
    public void configuraPathFitxerSortidaJson(String filePath) {
        this.filePathJson = filePath;
    }

    // Getter per obtenir el path XML
    public String obtenirRutaFitxerSortidaXML() {
        return this.filePathXML;
    }

    // Getter per obtenir el path JSON
    public String obtenirRutaFitxerSortidaJson() {
        return this.filePathJson;
    }
}