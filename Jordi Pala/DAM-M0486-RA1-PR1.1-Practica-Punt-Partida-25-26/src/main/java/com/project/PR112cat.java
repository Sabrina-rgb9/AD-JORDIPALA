package com.project;

import java.nio.file.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PR112cat {

    public static void main(String[] args) {
        // Comprovar que s'ha proporcionat una ruta com a paràmetre
        if (args.length == 0) {
            System.out.println("No s'ha proporcionat cap ruta d'arxiu.");
            return;
        }

        // Obtenir la ruta del fitxer des dels paràmetres
        String rutaArxiu = args[0];
        mostrarContingutArxiu(rutaArxiu);
    }

    // Funció per mostrar el contingut de l'arxiu o el missatge d'error corresponent
    public static void mostrarContingutArxiu(String rutaArxiu) {
        Path path = Paths.get(rutaArxiu);
        try {
            if (!Files.exists(path) || !Files.isReadable(path)) {
                System.out.println("El fitxer no existeix o no és accessible.");
                return;
            }
            if (Files.isDirectory(path)) {
                System.out.println("El path no correspon a un arxiu, sinó a una carpeta.");
                return;
            }
            
            Files.lines(path, StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("El fitxer no existeix o no és accessible.");
        }
    }
}
