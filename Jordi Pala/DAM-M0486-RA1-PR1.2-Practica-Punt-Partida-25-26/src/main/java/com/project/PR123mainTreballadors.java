package com.project;

import com.project.excepcions.IOFitxerExcepcio;
import com.project.utilitats.UtilsCSV;

import java.util.List;
import java.util.Scanner;

public class PR123mainTreballadors {
    private String filePath = System.getProperty("user.dir") + "/data/PR123treballadors.csv";
    private Scanner scanner = new Scanner(System.in);

    // Getters i setters per a filePath
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void iniciar() {
        boolean sortir = false;

        while (!sortir) {
            try {
                // Mostrar menú
                mostrarMenu();

                // Llegir opció de l'usuari
                int opcio = Integer.parseInt(scanner.nextLine());

                switch (opcio) {
                    case 1 -> mostrarTreballadors();
                    case 2 -> modificarTreballadorInteractiu();
                    case 3 -> {
                        System.out.println("Sortint...");
                        sortir = true;
                    }
                    default -> System.out.println("Opció no vàlida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Si us plau, introdueix un número vàlid.");
            } catch (IOFitxerExcepcio e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    // Mètode que mostra el menú
    private void mostrarMenu() {
        System.out.println("\nMenú de Gestió de Treballadors");
        System.out.println("1. Mostra tots els treballadors");
        System.out.println("2. Modificar dades d'un treballador");
        System.out.println("3. Sortir");
        System.out.print("Selecciona una opció: ");
    }

    // Mètode per mostrar els treballadors llegint el fitxer CSV
    public void mostrarTreballadors() throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/
        List<String> treballadors = llegirFitxerCSV();
        treballadors.forEach(System.out::println);
    }

    // Mètode per modificar un treballador (interactiu)
    public void modificarTreballadorInteractiu() throws IOFitxerExcepcio {
        // Demanar l'ID del treballador
        System.out.print("\nIntrodueix l'ID del treballador que vols modificar: ");
        String id = scanner.nextLine();

        // Demanar quina dada vols modificar
        System.out.print("Quina dada vols modificar (Nom, Cognom, Departament, Salari)? ");
        String columna = scanner.nextLine();

        // Demanar el nou valor
        System.out.print("Introdueix el nou valor per a " + columna + ": ");
        String nouValor = scanner.nextLine();

        // Modificar treballador
        modificarTreballador(id, columna, nouValor);
    }

    // Mètode que modifica treballador (per a tests i usuaris) llegint i escrivint sobre disc
    public void modificarTreballador(String id, String columna, String nouValor) throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/
        List<String> treballadorsCSV = llegirFitxerCSV();

        // Trobar la línia del treballador amb l'ID proporcionat
        int numLinia = -1;
        for (int i = 1; i < treballadorsCSV.size(); i++) { // començant per 1 per saltar la capçalera
            String[] dades = treballadorsCSV.get(i).split(",");
            if (dades[0].trim().equals(id.trim())) {
                numLinia = i;
                break;
            }
        }

        if (numLinia == -1) {
            throw new IOFitxerExcepcio("Treballador no trobat amb ID: " + id);
        }

        // Trobar l'índex de la columna a modificar
        String[] capcalera = treballadorsCSV.get(0).split(",");
        int indexColumna = -1;
        for (int i = 0; i < capcalera.length; i++) {
            if (capcalera[i].trim().equalsIgnoreCase(columna.trim())) {
                indexColumna = i;
                break;
            }
        }

        if (indexColumna == -1) {
            throw new IOFitxerExcepcio("Columna desconeguda: " + columna);
        }

        // Modificar el valor a la línia corresponent
        String[] dades = treballadorsCSV.get(numLinia).split(",");
        dades[indexColumna] = nouValor.trim();
        treballadorsCSV.set(numLinia, String.join(",", dades));

        // Escriure el fitxer actualitzat
        escriureFitxerCSV(treballadorsCSV);
    }

    // Encapsulació de llegir el fitxer CSV
    private List<String> llegirFitxerCSV() throws IOFitxerExcepcio {
        List<String> treballadorsCSV = UtilsCSV.llegir(filePath);
        if (treballadorsCSV == null) {
            throw new IOFitxerExcepcio("Error en llegir el fitxer.");
        }
        return treballadorsCSV;
    }

    // Encapsulació d'escriure el fitxer CSV
    private void escriureFitxerCSV(List<String> treballadorsCSV) throws IOFitxerExcepcio {
        try {
            UtilsCSV.escriure(filePath, treballadorsCSV);
        } catch (Exception e) {
            throw new IOFitxerExcepcio("Error en escriure el fitxer.", e);
        }
    }

    // Mètode main
    public static void main(String[] args) {
        PR123mainTreballadors programa = new PR123mainTreballadors();
        programa.iniciar();
    }    
}
