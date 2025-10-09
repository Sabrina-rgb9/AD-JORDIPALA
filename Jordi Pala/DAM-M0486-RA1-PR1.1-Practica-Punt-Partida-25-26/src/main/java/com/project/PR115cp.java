package com.project;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.List;

public class PR115cp {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Ús: java PR115cp <fitxer_origen> <fitxer_desti>");
            return;
        }

        String rutaOrigen = args[0];
        String rutaDesti = args[1];

        copiarFitxer(rutaOrigen, rutaDesti);
    }

    public static void copiarFitxer(String rutaOrigen, String rutaDesti) {
        Path origen = Paths.get(rutaOrigen);
        Path desti = Paths.get(rutaDesti);

        // Comprovar si el fitxer origen existeix i és un fitxer
        if (!Files.exists(origen) || !Files.isRegularFile(origen)) {
            System.out.println("El fitxer d'origen no existeix o no és un fitxer.");
            return;
        }

        // Comprovar si el fitxer de destinació existeix
        if (Files.exists(desti)) {
            System.out.println("Advertència: El fitxer de destinació ja existeix i serà sobreescrit.");
        }

        try {
            // Llegir el contingut del fitxer origen línia a línia amb UTF-8
            List<String> linies = Files.readAllLines(origen, StandardCharsets.UTF_8);

            // Crear el directori de destinació si no existeix
            Path directoriDesti = desti.getParent();
            if (directoriDesti != null && !Files.exists(directoriDesti)) {
                Files.createDirectories(directoriDesti);
            }

            // Escriure el contingut al fitxer de destinació amb UTF-8
            Files.write(desti, linies, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("La còpia s'ha realitzat correctament.");
        } catch (IOException e) {
            System.out.println("Error en copiar el fitxer: " + e.getMessage());
        }
    }
}
