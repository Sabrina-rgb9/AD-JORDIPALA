package com.project;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Random;

public class PR114linies {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiFitxer = System.getProperty("user.dir") + "/data/numeros.txt";

        // Crida al mètode que genera i escriu els números aleatoris
        generarNumerosAleatoris(camiFitxer);
    }

    // Mètode per generar 10 números aleatoris i escriure'ls al fitxer
    public static void generarNumerosAleatoris(String camiFitxer) {
        Path fitxer = Paths.get(camiFitxer);
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int num = random.nextInt(100); // 0-99
            sb.append(num);
            if (i < 9) {
                sb.append(System.lineSeparator());
            }
        }

        try {
            // Crear el directori "data" si no existeix
            Path directori = fitxer.getParent();
            if (directori != null && !Files.exists(directori)) {
                Files.createDirectories(directori);
            }
            // Escriure el contingut al fitxer amb UTF-8
            Files.write(fitxer, sb.toString().getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error escrivint al fitxer: " + e.getMessage());
        }
    }
}
