package com.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import com.project.excepcions.IOFitxerExcepcio;
import com.project.objectes.PR121hashmap;

public class PR121mainLlegeix {
    private static String filePath = System.getProperty("user.dir") + "/data/PR121HashMapData.ser";

    public static void main(String[] args) {
        try {
            PR121hashmap hashMap = deserialitzarHashMap();
            hashMap.getPersones().forEach((nom, edat) -> System.out.println(nom + ": " + edat + " anys"));
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error al llegir l'arxiu: " + e.getMessage());
        }
    }

    public static PR121hashmap deserialitzarHashMap() throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/

        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new IOFitxerExcepcio("Error en deserialitzar l'objecte HashMap: el fitxer no existeix");
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (PR121hashmap) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOFitxerExcepcio("Error en deserialitzar l'objecte HashMap: " + e.getMessage());
        }
    }

    // Getter
    public static String getFilePath() {
        return filePath;
    }

    // Setter
    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }    
}