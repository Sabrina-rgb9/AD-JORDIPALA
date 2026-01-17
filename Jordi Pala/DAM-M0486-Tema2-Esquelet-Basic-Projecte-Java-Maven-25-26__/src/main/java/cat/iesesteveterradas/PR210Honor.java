package cat.iesesteveterradas;

import cat.iesesteveterradas.utils.UtilsSQLite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;




public class PR210Honor {
    
// Verifica si la base de dades existeix.
// En cas contrari, inicialitza la base de dades amb les taules següents:
// Taula Facció:
// id: Clau primària, tipus enter.
// nom: Cadena de text (varchar) de màxim 15 caràcters.
// resum: Cadena de text (varchar) de màxim 500 caràcters.
// Taula Personatge:
// id: Clau primària, tipus enter.
// nom: Cadena de text (varchar) de màxim 15 caràcters.
// atac: Número real.
// defensa: Número real.
// idFaccio: Clau forània que enllaça amb l’id de la taula Facció.

    private static final Logger logger = LoggerFactory.getLogger(PR210Honor.class);

    public static void main(String[] args) {
    
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "pr210honor.db";

        try {
            File dbFile = new File(filePath);
            if (!dbFile.exists()) {
                // La base de dades no existeix, inicialitzar-la
                initDatabase(filePath);
        } else {
                logger.info("La base de dades ja existeix a {}", filePath);
            }

            try (Connection conn = UtilsSQLite.connect(filePath)) {

                Scanner sc = new Scanner(System.in);

                boolean salir = false;
                while (!salir) {
                    System.out.println("Menú:");
                    System.out.println("1. Mostrar una taula");
                    System.out.println("2. Mostrar personatges per facció");
                    System.out.println("3. Millor atacant per facció");
                    System.out.println("4. Millor defensor per facció");
                    System.out.println("5. Sortir");
                    System.out.print("Tria una opció: ");

                    String opc = sc.nextLine().trim();
                    switch (opc) {
                        case "1" -> {
                            System.out.print("Introdueix el nom de la taula (Faccio/Personatge): ");
                            String tabla = sc.nextLine().trim();
                            mostrarTabla(conn, tabla);
                        }
                        case "2" -> {
                            System.out.print("Introdueix el nom de la facció: ");
                            String faccio = sc.nextLine().trim();
                            mostrarPersonajesPorFaccion(conn, faccio);
                        }
                        case "3" -> {
                            System.out.print("Introdueix el nom de la facció: ");
                            String faccio = sc.nextLine().trim();
                            mejorAtacantePorFaccion(conn, faccio);
                        }
                        case "4" -> {
                            System.out.print("Introdueix el nom de la facció: ");
                            String faccio = sc.nextLine().trim();
                            mejorDefensorPorFaccion(conn, faccio);
                        }
                        case "5" -> {
                            System.out.println("Sortint de l'aplicació...");
                            salir = true;
                        }
                        default -> System.out.println("Opció invàlida. Torna a intentar-ho.");
                    }
                    
                }
                sc.close();
            }
            
        } catch (SQLException e) {
            logger.error("Error en la gestió de la base de dades: {}", e.getMessage());
        } 
 
    }

    static void initDatabase(String filePath) throws SQLException {

            try (Connection conn = UtilsSQLite.connect(filePath)) { 
                
                // Crear taula Faccio
                UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Faccio (" +
                        "id INTEGER PRIMARY KEY," +
                        "nom VARCHAR(15) NOT NULL," +
                        "resum VARCHAR(500)" +
                        ");");
                
                // Crear taula Personatge
                UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Personatge (" +
                        "id INTEGER PRIMARY KEY," +
                        "nom VARCHAR(15) NOT NULL," +
                        "atac REAL," +
                        "defensa REAL," +
                        "idFaccio INTEGER," +
                        "FOREIGN KEY(idFaccio) REFERENCES Faccio(id)" +
                        ");");

                
                logger.info("Base de dades inicialitzada correctament a {}", filePath);


                /* 
                Informacion a rellenar en la base de datos: 
                INSERT INTO Faccio (nom, resum) VALUES ("Cavallers", "Though seen as a single group, the Knights are hardly unified. There are many Legions in Ashfeld, the most prominent being The Iron Legion.");
                INSERT INTO Faccio (nom, resum) VALUES ("Vikings",   "The Vikings are a loose coalition of hundreds of clans and tribes, the most powerful being The Warborn.");
                INSERT INTO Faccio (nom, resum) VALUES ("Samurais",  "The Samurai are the most unified of the three factions, though this does not say much as the Daimyos were often battling each other for dominance.");


                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Warden",      1, 3, 1);
                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Conqueror",   2, 2, 1);
                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Peacekeep",   2, 3, 1);


                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Raider",    3, 3, 2);
                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Warlord",   2, 2, 2);
                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Berserker", 1, 1, 2);


                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Kensei",  3, 2, 3);
                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Shugoki", 2, 1, 3);
                INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ("Orochi",  3, 2, 3);
 
                */

                // Inserir dades inicials a la taula Faccio
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Cavallers', 'Though seen as a single group, the Knights are hardly unified. There are many Legions in Ashfeld, the most prominent being The Iron Legion.');");
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Vikings',   'The Vikings are a loose coalition of hundreds of clans and tribes, the most powerful being The Warborn.');");
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Faccio (nom, resum) VALUES ('Samurais',  'The Samurai are the most unified of the three factions, though this does not say much as the Daimyos were often battling each other for dominance.');");
                
                logger.info("Dades inicials inserides a Faccio correctamente.");

                // Inserir dades inicials a la taula Personatge
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Warden',      1, 3, 1);");
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Conqueror',   2, 2, 1);");
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Peacekeep',   2, 3, 1);");

                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Raider',    3, 3, 2);");
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Warlord',   2, 2, 2);");
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Berserker', 1, 1, 2);");

                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Kensei',  3, 2, 3);");
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Shugoki', 2, 1, 3);");
                UtilsSQLite.queryUpdatePS(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES ('Orochi',  3, 2, 3);");
                
                logger.info("Dades inicials inserides a Personatge correctamente.");
            }
    }

    // METODOS DE LES OPCIONS DEL MENÚ

    static void mostrarTabla(Connection conn, String tabla) {
        // Implementar la lògica per mostrar la taula especificada

        String sql = "SELECT * FROM " + tabla + ";";
        try (ResultSet rs = UtilsSQLite.querySelect(conn, sql)) {
            System.out.println("Contingut de la taula " + tabla + ":");
            int colCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    System.out.print(rs.getString(i));
                    if (i < colCount) System.out.print(" | ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            logger.error("Error en mostrar la taula {}: {}", tabla, e.getMessage());
        }

    }

    static void mostrarPersonajesPorFaccion(Connection conn, String faccio) {
        // Implementar la lògica per mostrar personatges per facció
        String sql = """
            SELECT P.nom, P.atac, P.defensa, F.nom AS faccio
            FROM Personatge P
            JOIN Faccio F ON P.idFaccio = F.id
            WHERE F.nom = ?;
        """;
        try (ResultSet rs = UtilsSQLite.querySelectPS(conn, sql, faccio)) {
            System.out.println("Personatges de la facció " + faccio + ":");
            while (rs.next()) {
                System.out.println("Nom: " + rs.getString("nom") +
                                   ", Atac: " + rs.getDouble("atac") +
                                   ", Defensa: " + rs.getDouble("defensa"));
            }
        } catch (Exception e) {
            logger.error("Error en mostrar personatges per facció {}: {}", faccio, e.getMessage());
        }   
    }

    static void mejorAtacantePorFaccion(Connection conn, String faccio) {
        // Implementar la lògica per mostrar el millor atacant per facció

        String sql = """
            SELECT P.nom, P.atac, F.nom AS faccio
            FROM Personatge P
            JOIN Faccio F ON P.idFaccio = F.id
            WHERE F.nom = ?
            ORDER BY P.atac DESC
            LIMIT 1;
        """;
        try (ResultSet rs = UtilsSQLite.querySelectPS(conn, sql, faccio)) {
            if (rs.next()) {
                System.out.println("Millor atacant de la facció " + faccio + ": " +
                                   rs.getString("nom") + " (Atac: " + rs.getDouble("atac") + ")");
            } else {
                System.out.println("No hi ha personatges a la facció " + faccio);
            }
        } catch (Exception e) {
            logger.error("Error en mostrar el millor atacant per facció {}: {}", faccio, e.getMessage());
        }

    }

    static void mejorDefensorPorFaccion(Connection conn, String faccio) {
        // Implementar la lògica per mostrar el millor defensor per facció
        
        String sql = """
            SELECT P.nom, P.defensa, F.nom AS faccio
            FROM Personatge P
            JOIN Faccio F ON P.idFaccio = F.id
            WHERE F.nom = ?
            ORDER BY P.defensa DESC
            LIMIT 1;
        """;
        try (ResultSet rs = UtilsSQLite.querySelectPS(conn, sql, faccio)) {
            if (rs.next()) {
                System.out.println("Millor defensor de la facció " + faccio + ": " +
                                   rs.getString("nom") + " (Defensa: " + rs.getDouble("defensa") + ")");
            } else {
                System.out.println("No hi ha personatges a la facció " + faccio);
            }
        } catch (Exception e) {
            logger.error("Error en mostrar el millor defensor per facció {}: {}", faccio, e.getMessage());
        }

    }

}
