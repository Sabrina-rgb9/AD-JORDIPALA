package com.project;

import java.nio.file.*;
import java.io.IOException;

public class PR111Files {

    public static void main(String[] args) {
        String camiFitxer = System.getProperty("user.dir") + "/data/pr111";
        gestionarArxius(camiFitxer);
    }

    public static void gestionarArxius(String camiFitxer) {
        Path carpeta = Paths.get(camiFitxer, "myFiles");
        try {
            // 1. Crear carpeta myFiles
            if (!Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }

            // 2. Crear file1.txt i file2.txt
            Path file1 = carpeta.resolve("file1.txt");
            Path file2 = carpeta.resolve("file2.txt");
            Files.createFile(file1);
            Files.createFile(file2);

            // 3. Renombrar file2.txt a renamedFile.txt
            Path renamedFile = carpeta.resolve("renamedFile.txt");
            Files.move(file2, renamedFile, StandardCopyOption.REPLACE_EXISTING);

            // 4. Mostrar llistat d'arxius
            System.out.println("Els arxius de la carpeta son:");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta)) {
                for (Path entry : stream) {
                    System.out.println(" - " + entry.getFileName());
                }
            }

            // 5. Eliminar file1.txt
            Files.deleteIfExists(file1);

            // 6. Tornar a mostrar llistat d'arxius
            System.out.println("Els arxius de la carpeta son:");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta)) {
                for (Path entry : stream) {
                    System.out.println(" - " + entry.getFileName());
                }
            }

        } catch (IOException e) {
            System.err.println("Error amb els arxius: " + e.getMessage());
        }
    }
}