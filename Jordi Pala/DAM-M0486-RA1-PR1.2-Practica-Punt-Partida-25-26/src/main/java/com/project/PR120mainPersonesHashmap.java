package com.project;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.project.excepcions.IOFitxerExcepcio;

public class PR120mainPersonesHashmap {
    private static String filePath = System.getProperty("user.dir") + "/data/PR120persones.dat";

    public static void main(String[] args) {
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Anna", 25);
        persones.put("Bernat", 30);
        persones.put("Carla", 22);
        persones.put("David", 35);
        persones.put("Elena", 28);

        try {
            escriurePersones(persones);
            llegirPersones();
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error en treballar amb el fitxer: " + e.getMessage());
        }
    }

    // Getter per a filePath
    public static String getFilePath() {
        return filePath;
    }

    // Setter per a filePath
    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }

    // Mètode per escriure les persones al fitxer
    public static void escriurePersones(HashMap<String, Integer> persones) throws IOFitxerExcepcio {
       // *************** CODI PRÀCTICA **********************/

       try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
            for (Map.Entry<String, Integer> entry : persones.entrySet()) { // utilizamos esta iteración para recorrer el HASHMAP
                dos.writeUTF(entry.getKey()); //clave
                dos.writeInt(entry.getValue()); // valor 
            }
       } catch (Exception e) {
            throw new IOFitxerExcepcio("Error escrivint al fitxer: " + e.getMessage());
       }

    }

    // Mètode per llegir les persones des del fitxer
    public static void llegirPersones() throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) { // leemos los datos del fichero con DataInputStream 

            while (dis.available() > 0) { // con datos disponibles leemos clave/valor
                String nom = dis.readUTF();
                int edat = dis.readInt();
                System.out.println("Nom: " + nom + ", Edat: " + edat);
            }

        } catch (Exception e) {
            throw new IOFitxerExcepcio("Error llegint del fitxer: " + e.getMessage());
        }
    }
}
