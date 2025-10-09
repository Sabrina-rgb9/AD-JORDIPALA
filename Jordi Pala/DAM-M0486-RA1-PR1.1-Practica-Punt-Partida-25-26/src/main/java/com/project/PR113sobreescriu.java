package com.project;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PR113sobreescriu {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiFitxer = System.getProperty("user.dir") + "/data/frasesMatrix.txt";

        // Crida al mètode que escriu les frases sobreescrivint el fitxer
        escriureFrases(camiFitxer);
    }

    // Mètode que escriu les frases sobreescrivint el fitxer amb UTF-8 i línia en blanc final
    public static void escriureFrases(String camiFitxer) {
        Path fitxer = Paths.get(camiFitxer);
        List<String> frases = Arrays.asList(
            "I can only show you the door",
            "You're the one that has to walk through it",
            ""
        );
        try {
            // Crear el directori "data" si no existeix
            Path directori = fitxer.getParent();
            if (directori != null && !Files.exists(directori)) {
                Files.createDirectories(directori);
            }
            // Escriure les frases sobreescrivint el fitxer
            Files.write(fitxer, frases, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error escrivint al fitxer: " + e.getMessage());
        }
    }
}
