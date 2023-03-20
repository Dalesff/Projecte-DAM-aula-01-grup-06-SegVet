package m13_ales_f_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author David Ales Fernandez
 */
public class Server {

    private static final int PORT = 44556;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/SegVet";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "grup6";
    
    /**
     * Aquest Map s'utilitza per portar un registre de les sessions actives al servidor.
     * Cada vegada que un usuari inicia sessió, s'afegeix una entrada al mapa amb la clau corresponent al correu electrònic de l'usuari i el valor true.
     * Quan l'usuari tanca la sessió, s'elimina l'entrada corresponent del mapa.
     */
    private final Map<String, Boolean> activeSessions = new HashMap<>();

    /**
     * El mètode start() s'encarrega de configurar el servidor i engegar-lo.
     */
    public void start() {
        
        // L'objecte ServerSocket que escolteu al port 44556. Si la creació del socket es realitza amb èxit, el servidor imprimeix un missatge indicant que s'ha iniciat correctament.
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("S'ha iniciat el servidor");
            
            // El mètode estableix una connexió amb la base de dades utilitzant els paràmetres de connexió proporcionats (nom d'usuari, contrasenya i URL de la base de dades).
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                // // Si la connexió es fa amb èxit, el servidor imprimeix un missatge indicant que s'ha establert correctament.
                System.out.println("Connexió a la base de dades establerta correctament");
                
                // El bucle infinit que accepta connexions entrants de clients.
                while (true) {
                    // El fil d'execució es crea utilitzant una expressió lambda que truca al mètode handleRequest() per processar la sol·licitud.
                    Socket socket = serverSocket.accept();
                    new Thread(() -> handleRequest(socket, connection)).start();
                }
            // Si es produeix un error en establir la connexió amb la base de dades o en iniciar el socket del servidor, el mètode captura l'excepció corresponent i mostra un missatge d'error a la consola.
            } catch (SQLException ex) {
                System.out.println("Error en establir la connexió a la base de dades: " + ex.getMessage());
            }

        } catch (IOException ex) {
            System.out.println("Error en iniciar el servidor: " + ex.getMessage());
        }
    }

    /**
     * El mètode handleRequest() s'encarrega de processar les sol·licituds d'inici i tancament de sessió.
     * @param socket
     * @param connection 
     */
    private void handleRequest(Socket socket, Connection connection) {
        
        try (Scanner scanner = new Scanner(socket.getInputStream());
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Si la sol·licitud comença amb un String "login", s'extreuen les credencials d'usuari del missatge i es valida utilitzant el mètode validateCredentials().
                if (line.startsWith("login")) {
                    String[] parts = line.split(":");
                    String email = parts[1];
                    String password = parts[2];
                    // Si les credencials són vàlides, s'envia un missatge d'èxit d'inici de sessió al client i s'afegeix una entrada al mapa activeSessions que indica que l'usuari està actiu.
                    if (validateCredencials(email, password, connection)) {
                        writer.println("login_success");
                        System.out.println("User " + email + " logged in");
                        activeSessions.put(email, true);
                        while (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                            // Quan es rep el missatge de tancament de sessió, s'elimina l'entrada corresponent al mapa activeSessions, s'envia un missatge d'èxit de tancament de sessió al client i es tanca la connexió amb el client.
                            if (line.equals("logout")) {
                                activeSessions.remove(email);
                                writer.println("logout_success");
                                System.out.println("User " + email + " logged out");
                                break;
                            }
                        }
                    } else {
                        writer.println("login_error");
                    }
                } else {
                    writer.println("unknown_command");
                }
            }
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error de gestió de la sol·licitud: " + ex.getMessage());
        }
    }

    /**
     * El mètode validateCredentials() s'encarrega de validar les credencials d'usuari.
     * @param email
     * @param password
     * @param connection
     * @return 
     */
    private boolean validateCredencials(String email, String password, Connection connection) {
        // Realitza una consulta a la base de dades per cercar un registre a la taula usuaris que tingui el correu electrònic i la contrasenya proporcionats.
        // Si la consulta torna un registre, les credencials són vàlides i el mètode torna true. En cas contrari, torna false.
        String query = "SELECT * FROM usuaris WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Error en executar la consulta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mètode per accedir al map de les sessions activas.
     * @return 
     */
    public Map<String, Boolean> getActiveSessions() {
        return activeSessions;
    }
}
