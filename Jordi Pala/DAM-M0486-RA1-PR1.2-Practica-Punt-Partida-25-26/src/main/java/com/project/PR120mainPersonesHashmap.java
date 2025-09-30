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
    try {
        // Crear carpeta si no existeix
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // només crea la carpeta

        // Escriure dades
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            for (Map.Entry<String, Integer> entry : persones.entrySet()) {
                dos.writeUTF(entry.getKey());  // nom
                dos.writeInt(entry.getValue()); // edat
            }
        }

    } catch (Exception e) {
        throw new IOFitxerExcepcio("Error escrivint al fitxer: " + e.getMessage());
    }
}

public static void llegirPersones() throws IOFitxerExcepcio {
    File file = new File(filePath);
    if (!file.exists()) {
        throw new IOFitxerExcepcio("El fitxer no existeix: " + filePath);
    }

    try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
        while (dis.available() > 0) {
            String nom = dis.readUTF();
            int edat = dis.readInt();
            System.out.println(nom + ": " + edat + " anys");
        }
    } catch (Exception e) {
        throw new IOFitxerExcepcio("Error llegint del fitxer: " + e.getMessage());
    }
}
}