package m13_ales_f_server.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import m13_ales_f_server.controller.exceptions.NonexistentEntityException;
import m13_ales_f_server.model.Cita;
import m13_ales_f_server.model.EntradaHistorial;
import m13_ales_f_server.model.HistorialMedic;
import m13_ales_f_server.model.Mascota;
import m13_ales_f_server.model.Usuari;

/**
 * 
 * @author David Ales Fernandez
 */
public class Server {

    private static final int PORT = 44556;
    // keytool -genkey -v -alias server -keyalg RSA -keystore ./server_grup6 -dname "CN=localhost,OU=cn,O=cn,L=cn,ST=cn,C=cn" -storepass 123123 -keypass 111222
    // keytool -export -alias server -keystore ./server_grup6 -file server_key.cer
    // keytool -import -trustcacerts -alias client -file ./client_key.cer -keystore ./server_grup6
    private static String SERVIDOR_CLAU = "C:\\Users\\David\\server_grup6";
    private static String SERVIDOR_CLAU_PASSWORD = "111222";
    /**
     * Aquest Map s'utilitza per portar un registre de les sessions actives al servidor.
     * Cada vegada que un usuari inicia sessió, s'afegeix una entrada al mapa amb la clau corresponent al correu electrònic de l'usuari i el valor true.
     * Quan l'usuari tanca la sessió, s'elimina l'entrada corresponent del mapa.
     */
    private final Map<String, String> activeSessions = new HashMap<>();
    
    /**
     * El mètode start() s'encarrega de configurar el servidor i engegar-lo.
     */
    public void start() throws Exception {
        // Creació de l'EntityManagerFactory per a la persistència, utilitzant la unitat de persistència "segvet".
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("segvet");
        // Creació de l'EntityManager a partir de l'EntityManagerFactory.
        EntityManager em = emf.createEntityManager();
        // Es configura la propietat del sistema "javax.net.ssl.trustStore" amb el valor de SERVIDOR_CLAU, que representa el fitxer del keystore de confiança del servidor.
        System.setProperty("javax.net.ssl.trustStore", SERVIDOR_CLAU);
        // S'obté una instància de SSLContext utilitzant el protocol "TLS".
        SSLContext context = SSLContext.getInstance("TLS");
        // S'obté una instància de KeyStore utilitzant el tipus "jceks".
        KeyStore ks = KeyStore.getInstance("jceks");
        // Es carrega el keystore utilitzant el fitxer SERVIDOR_CLAU, amb contrasenya nul·la.
        ks.load(new FileInputStream(SERVIDOR_CLAU), null);
        // S'obté una instància de KeyManagerFactory utilitzant l'algorisme "SunX509".
        KeyManagerFactory kf = KeyManagerFactory.getInstance("SunX509");
        // Es configura el KeyManagerFactory amb el keystore (ks) i la contrasenya (SERVIDOR_CLAU_PASSWORD) del servidor.
        kf.init(ks, SERVIDOR_CLAU_PASSWORD.toCharArray());
        // S'inicialitza el context SSL amb els KeyManagers obtinguts anteriorment i sense TrustManagers ni SecureRandom.
        context.init(kf.getKeyManagers(), null, null);
        // S'obté una instància de ServerSocketFactory del context SSL.
        ServerSocketFactory factory = context.getServerSocketFactory();
        // L'objecte ServerSocket que escolteu al port 44556. Si la creació del socket es realitza amb èxit, el servidor imprimeix un missatge indicant que s'ha iniciat correctament.
        try (ServerSocket  serverSocket = factory.createServerSocket(PORT)) {
            // S'estableix la necessitat d'autenticació del client en true per al socket del servidor.
            ((SSLServerSocket) serverSocket).setNeedClientAuth(true);
            
            System.out.println("S'ha iniciat el servidor");
            // El bucle infinit que accepta connexions entrants de clients.
            while (true) {
                // El fil d'execució es crea utilitzant una expressió lambda que truca al mètode handleRequest() per processar la sol·licitud.
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                new Thread(() -> {
                    try {
                        handleRequest(socket, em, emf);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();
            } 
        } catch (IOException ex) {
            System.out.println("Error en iniciar el servidor: " + ex.getMessage());
        }
        
        emf.close();
        em.close();   
    }
    
    /**
     * El mètode handleRequest() s'encarrega de processar les sol·licituds d'inici i tancament de sessió.
     */
    private void handleRequest(Socket socket, EntityManager em,EntityManagerFactory emf) throws NoSuchAlgorithmException {
   
        try (Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                // Aquí evalua si arriba una peticio correctment o be de login, o de registrar a un nou usuari (crear_usuari), si arriba un altre tipus de petició donará error.
                if (!line.matches("(login|crea_usuari):.*")) {
                    writer.println("unknown_command 1");
                } else {
                    // Aquí arriba una petició de login d'un usuari ya existent y registrat a la base de dades.
                    if (line.startsWith("login")) {
                        UUID uuid = UUID.randomUUID();
                        String sessionId = uuid.toString();
                        String[] parts = line.split(":");
                        String email = parts[1];
                        // Aquí s'evalua si les dades son correctes.
                        if (login(parts, em, writer, sessionId)) {
                            // Una vegada verificat que les dades per accedir al sistema son correctes, l'usuari que ha fet login pot accedir al menú.
                            menu(scanner, em, emf, writer, email, sessionId);
                        }
                    // Aquí arriba una peticio per crear un nou usuari per accedir al sistema.
                    } else if (line.startsWith("crea_usuari")) {
                        crearUsuari(line, emf, writer);
                    } else {
                        writer.println("unknown_command 2");
                    }
                }
            }
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error de gestió de la sol·licitud: " + ex.getMessage());
        }
    }
    
    // Aquest es el menú, el cual evaluará la petició que arribará desde el client.
    private void menu(Scanner scanner, EntityManager em, EntityManagerFactory emf, PrintWriter writer, String email, String sessionId) throws NoSuchAlgorithmException {
        boolean login = true;
        while (login) {
            
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            int idUsuari = findUsuariIdByEmail(em, email);
            Usuari usuari = new UsuariJpaController(emf).findUsuari(idUsuari);

            switch (parts[0]){
                case "logout":
                    logout(sessionId, writer);
                    login = false;
                    break;
                case "usuari":
                    if (parts[1].startsWith("consulta_usuari")) {
                        consultaUsuari(usuari, writer);
                        break;
                    } else if (parts[1].startsWith("edita_usuari")) {
                        try {
                            editarUsuari(usuari, parts, emf, writer);
                        } catch (NonexistentEntityException e) {
                        }
                        break;
                    } else if (parts[1].startsWith("borra_usuari")) {
                        try {
                            borrarUsuari(usuari, emf, writer);
                            logout(sessionId, writer);
                            login = false;
                        } catch (NonexistentEntityException e) {
                        }
                        break;
                    }else {
                        writer.println("unknown_command 4");
                        break;
                    }
                case "mascota":
                    if (parts[1].startsWith("crea_mascota")) {
                        crearMascota(usuari, parts, emf, writer);
                        break;
                    } else if (parts[1].startsWith("consulta_mascota")) {
                        llistaMascotas(consultaListaMascota(idUsuari, em), writer);
                        
                        break;
                    } else if (parts[1].startsWith("edita_mascota")) {
                        editaMascota(usuari, parts, em, emf, writer);
                        break;
                    } else if (parts[1].startsWith("borra_mascota")) {
                        try {
                            borraMascota(parts, em, emf, writer);
                        } catch (NonexistentEntityException e) {
                        }
                        break;
                    }else {
                        writer.println("unknown_command 5");
                        break;
                    }
                case "historial":
                    if (parts[1].startsWith("consulta_historial")) {
                        llistaEntrades(consultaLlistaEntrades(parts, em), writer);
                        break;
                    } else if (parts[1].startsWith("crea_entrada")) {
                        crearEntrada(parts, em, emf, writer);
                        break;
                    } else if (parts[1].startsWith("consulta_entrada")) {
                        consultaEntrada(parts, em, writer);
                        break;
                    } else if (parts[1].startsWith("edita_entrada")) {
                        editaEntrada(parts, em, emf, writer);
                        break;
                    } else if (parts[1].startsWith("borra_entrada")) {
                        try {
                            borraEntrada(parts, em, emf, writer);
                            break;
                        } catch (NonexistentEntityException e) {
                        }
                    }else {
                        writer.println("unknown_command 6");
                        break;
                    }
                case "cita":
                    if (parts[1].startsWith("crea_cita")) {
                        creaCita(usuari, parts, em, emf, writer);
                        break;
                    } else if (parts[1].startsWith("consulta_cita_mascota")) {
                        llitaCitas(consultaLlistaCitaMascota(parts, em), writer);
                        break;
                    } else if (parts[1].startsWith("consulta_cita_veterinari")) {
                        llitaCitas(consultaLlistaCitaVeterinari(usuari, em), writer);
                        break;
                    } else if (parts[1].startsWith("edita_cita")) {
                        editaCita(parts, emf, writer);
                        break;
                    } else if (parts[1].startsWith("borra_cita")) {
                        try{
                            borraCita(parts, emf, writer);
                            break;
                        } catch (NonexistentEntityException e) {
                        }
                    }else {
                        writer.println("unknown_command 7");
                        break;
                    }
                default:
                    writer.println("unknown_command 3");
                    break;
            }
        }
    }
    
    // El mètode hashPassword rep una contrasenya en forma de String i torna el seu hash en forma de String.
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : byteData) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

     /**
     * El mètode validateCredentials() s'encarrega de validar les credencials d'usuari.
     */
    private boolean validateCredencials(String email, String password, EntityManager em) {
        // Realitza una consulta a la base de dades per cercar un registre a la taula usuaris que tingui el correu electrònic i la contrasenya proporcionats.
        // Si la consulta torna un registre, les credencials són vàlides i el mètode torna true. En cas contrari, torna false.
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", password);
        
        TypedQuery<Usuari> query = em.createQuery("SELECT u FROM Usuari u WHERE u.email = :email AND u.password = :password", Usuari.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        try {
            return query.getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }
    
    /**
     * Mètode per accedir al map de les sessions activas.
     */
    public Map<String, String> getActiveSessions() {
        return activeSessions;
    }
    
    // Aquest métode evalua si les credencials introduides son correctes, i si pot accedir al sistema.
    private boolean login(String[] parts, EntityManager em, PrintWriter writer, String sessionId) throws NoSuchAlgorithmException {
        
        String email = parts[1];
        String password = hashPassword(parts[2]);
        
        if (activeSessions.containsValue(email)) {
            writer.println("Aquest usuari ja esta dins del sistema");
            return false;
        } else {
            if (validateCredencials(email, password, em)) {
                activeSessions.put(sessionId, email);
                writer.println("login_success" + ":" + sessionId);
                return true;
            } else {
                writer.println("login_error");
                return false;
            }
        }
    }
    
    // Aquest métode s'utilitza per que un usuari surti del sistema.
    private void logout(String sessionId, PrintWriter writer) {
        activeSessions.remove(sessionId);
        writer.println("logout_success");
    }
    
    // Aquest métode s'utilitza per trobar l'id de l'usuari que ha fet login per mitjá de seu email.
    public Integer findUsuariIdByEmail(EntityManager em, String email) {
        TypedQuery<Integer> query = em.createQuery(
                "SELECT u.id FROM Usuari u WHERE u.email = :email", Integer.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
    
    // Aquest es el métode que crea un nou usuari i el registra a la base de dades.
    private void crearUsuari(String line,  EntityManagerFactory emf, PrintWriter writer) throws NoSuchAlgorithmException {
        String[] reg = line.split(":");
        String hashedPassword = hashPassword(reg[5]);
        
        Usuari nouUsuari = new Usuari();
        nouUsuari.setNom(reg[1]);
        nouUsuari.setCognom1(reg[2]);
        nouUsuari.setCognom2(reg[3]);
        nouUsuari.setEmail(reg[4]);
        nouUsuari.setPassword(hashedPassword);
        nouUsuari.setDireccio(reg[6]);
        nouUsuari.setTelefon(reg[7]);
        nouUsuari.setRol(reg[8]);
        
        new UsuariJpaController(emf).create(nouUsuari);
        writer.println("register_success");
    }
    
    // Aquest es el métode que fa la consulta i retorna les dades del usuari.
    private void consultaUsuari(Usuari usuari, PrintWriter writer) {
        writer.println(usuari.getNom() + ":" + usuari.getCognom1() + ":" + usuari.getCognom2() + ":" + usuari.getEmail() + ":" + usuari.getDireccio() + ":" + usuari.getTelefon());
    }
    
    // Aquest es el métode que s'utilitza per modificar les dades d'usuari.
    private void editarUsuari(Usuari usuari, String[] reg, EntityManagerFactory emf, PrintWriter writer) throws NonexistentEntityException, NoSuchAlgorithmException {
        String hashedPassword = hashPassword(reg[6]);
        
        usuari.setNom(reg[2]);
        usuari.setCognom1(reg[3]);
        usuari.setCognom2(reg[4]);
        usuari.setEmail(reg[5]);
        usuari.setPassword(hashedPassword);
        usuari.setDireccio(reg[7]);
        usuari.setTelefon(reg[8]);
        usuari.setRol(reg[9]);
        
        try {
            new UsuariJpaController(emf).edit(usuari);
            writer.println("update_success");
        } catch (Exception e) {
        }
        
    }
    
    // Aquest es el métode que s'utilitza per eliminar a un usuari del sistema.
    private void borrarUsuari(Usuari usuari, EntityManagerFactory emf, PrintWriter writer) throws NonexistentEntityException {
        new UsuariJpaController(emf).destroy(usuari.getId());
        writer.println("delete_success");
    }
    
    // Aquest es el métode que crea una nova mascota de l'usuari que ha fet login previament.
    private void crearMascota(Usuari usuari, String[] reg, EntityManagerFactory emf, PrintWriter writer) {
        int edad = Integer.parseInt(reg[3]);
        char sex = reg[6].charAt(0);
        
        Mascota novaMascota = new Mascota();
        HistorialMedic hm = new HistorialMedic();
        
        new HistorialMedicJpaController(emf).create(hm);
        
        novaMascota.setNom(reg[2]);
        novaMascota.setEdad(edad);
        novaMascota.setEspecie(reg[4]);
        novaMascota.setRaza(reg[5]);
        novaMascota.setSexo(sex);
        
        novaMascota.setIdHistorial(hm);
        novaMascota.setIdUsuari(usuari);
        
        new MascotaJpaController(emf).create(novaMascota);
        writer.println("register_success");
    }
    
    // Aquest es el métode que retorna la llista de mascotas de l'usuari.
    private List consultaListaMascota(int id, EntityManager em) {
        Query consulta = em.createQuery("SELECT m FROM Mascota m WHERE m.idUsuari.id = :idUsuari").setParameter("idUsuari", id);
        List<Mascota> mascotas = consulta.getResultList();
        return mascotas;
    }
    
    // Aquest métode envía la llista de mascotes al client desde el cual s'ha accedit al servidor.
    private void llistaMascotas(List<Mascota> mascotas, PrintWriter writer) {
        for (Mascota mascota : mascotas) {
            writer.println(mascota.getId() + ":" + mascota.getNom() + ":" + mascota.getEdad() + ":" + mascota.getEspecie() + ":" + mascota.getRaza() + ":" + mascota.getSexo());
        }
    }
    
    // Aquest es el métode que s'utilitza per editar les dades de les mascotes, ha d'arribar l'id de la mascota per parametre.
    private void editaMascota(Usuari usuari, String[] reg, EntityManager em, EntityManagerFactory emf, PrintWriter writer) {
        int id = Integer.parseInt(reg[2]);
        Mascota actuMascota = (Mascota) em.createNamedQuery("Mascota.findById").setParameter("id", id).getSingleResult();
        int edad = Integer.parseInt(reg[4]);
        char sex = reg[7].charAt(0);
        
        actuMascota.setNom(reg[3]);
        actuMascota.setEdad(edad);
        actuMascota.setEspecie(reg[5]);
        actuMascota.setRaza(reg[6]);
        actuMascota.setSexo(sex);
        actuMascota.setIdUsuari(usuari);
        
        try {
            new MascotaJpaController(emf).edit(actuMascota);
            writer.println("update_success");
        } catch (Exception e) {
        
        }
    }
    
    // Aquest es el métode que elimina a una mascota del sistema, ha d'arribar l'id de la mascota per parametre.
    private void borraMascota(String[] reg, EntityManager em, EntityManagerFactory emf, PrintWriter writer) throws NonexistentEntityException {
        int id = Integer.parseInt(reg[2]);
        Mascota mascota = (Mascota) em.createNamedQuery("Mascota.findById").setParameter("id", id).getSingleResult();
        
        new MascotaJpaController(emf).destroy(mascota.getId());
        writer.println("delete_success");
    }
    
    // Aquest es el metode que retorna l'historial médic de la mascota en questió, amb totes les entrades sobre les incidents de les mascotes, ha d'arribar per parametre l'id de la mascota que es vole coneixe el seu historial.
    private List consultaLlistaEntrades(String[] reg, EntityManager em) {
        int id = Integer.parseInt(reg[2]);
        
        Query consultaHistorial = em.createQuery("SELECT h FROM HistorialMedic h WHERE h.idMascota.id = :idMascota").setParameter("idMascota", id);
        consultaHistorial.setParameter("idMascota", id);
        HistorialMedic hm = (HistorialMedic) consultaHistorial.getSingleResult();
        
        Query consultaEntradas = em.createQuery("SELECT e FROM EntradaHistorial e WHERE e.idHistorial.id = :idHistorial");
        consultaEntradas.setParameter("idHistorial", hm.getId());
        List<EntradaHistorial> entradas = consultaEntradas.getResultList();
        return entradas;
    }
    
    // Métode que imprimeix en pantalla del client que ha accedit al servidor la llista d'entrades.
    private void llistaEntrades(List<EntradaHistorial> entradas, PrintWriter writer) {
        writer.println("Entradas a l'historial medic:");
        for (EntradaHistorial entrada : entradas) {
            writer.println("- " + entrada.getDescripcio());
        }
    }
    
    // Aquest es el métode per crear una nova entrada del historial médic, ha d'arribar per parametre la id de l'historial médic al cual es vol crear la nova entrada.
    private void crearEntrada(String[] reg, EntityManager em, EntityManagerFactory emf, PrintWriter writer) {
        int id = Integer.parseInt(reg[2]);
        HistorialMedic historial = (HistorialMedic) em.createNamedQuery("HistorialMedic.findById").setParameter("id", id).getSingleResult();
        
        EntradaHistorial novaEntrada = new EntradaHistorial();
        novaEntrada.setData(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        novaEntrada.setDescripcio(reg[3]);
        novaEntrada.setIdHistorial(historial);
        
        new EntradaHistorialJpaController(emf).create(novaEntrada);
        writer.println("register_success");
    }
    
    // Aquest es el métode per visualitzar el contingut d'una entrada en cuestió, ha d'arribar per parametre la id de la entrada a la que es vol accedir.
    private void consultaEntrada(String[] reg, EntityManager em, PrintWriter writer) {
        int id = Integer.parseInt(reg[2]);
        Query consulta = em.createQuery("SELECT e FROM EntradaHistorial e WHERE e.id = :id");
        consulta.setParameter("id", id);
        EntradaHistorial entrada = (EntradaHistorial) consulta.getSingleResult();
        writer.println("Entrada: " + entrada.getId() + ":" + entrada.getDescripcio());
    }
    
    // Aquest es el métode per editar el contingut d'una entrada en questió, ha d'arribar per parametre la id de la entrada a la que es vol editr.
    private void editaEntrada(String[] reg, EntityManager em, EntityManagerFactory emf, PrintWriter writer) {
        int id = Integer.parseInt(reg[2]);
        Query consulta = em.createQuery("SELECT e FROM EntradaHistorial e WHERE e.id = :id");
        consulta.setParameter("id", id);
        EntradaHistorial actuEntrada = (EntradaHistorial) consulta.getSingleResult();
        
        actuEntrada.setDescripcio(reg[3]);
        
        try {
            new EntradaHistorialJpaController(emf).edit(actuEntrada);
            writer.println("update_success");
        } catch (Exception e) {
        }
    }
    
    // Aquest es el métode per eliminar una entrada del sistema, ha d'arribar per parametre la id de la entrada a la que es vol borrar.
    private void borraEntrada(String[] reg, EntityManager em, EntityManagerFactory emf, PrintWriter writer) throws NonexistentEntityException {
        int id = Integer.parseInt(reg[2]);
        EntradaHistorial entrada = (EntradaHistorial) em.createNamedQuery("EntradaHistorial.findById").setParameter("id", id).getSingleResult();
        
        new EntradaHistorialJpaController(emf).destroy(entrada.getId());
        writer.println("delete_success");
    }
    
    // Métode per crear una cita médica, ha d'arribar un string amb la data, descripció i l'id de la mascota.
    private void creaCita(Usuari veterinari, String[] reg, EntityManager em, EntityManagerFactory emf, PrintWriter writer) {

        int idMascota = Integer.parseInt(reg[4]);
        Mascota mascota = (Mascota) em.createNamedQuery("Mascota.findById").setParameter("id", idMascota).getSingleResult();
        
        Cita novaCita = new Cita();
        // La data ha d'arribar en un format similar a "2023-05-01".
        novaCita.setData(Date.from(LocalDate.parse(reg[2]).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        novaCita.setDescripcio(reg[3]);
        novaCita.setIdMascota(mascota);
        novaCita.setIdDueno(mascota.getIdUsuari());
        novaCita.setIdVeterinari(veterinari);
        
        new CitaJpaController(emf).create(novaCita);
        writer.println("register_success");
    }
    
    // Métode que retrona la llista de citas medicas d'una mascota, ha d'arribar l'id de la mascota.
    private List consultaLlistaCitaMascota(String[] reg, EntityManager em) {
        
        int id = Integer.parseInt(reg[2]);
        Query consultaCita = em.createQuery("SELECT h FROM Cita h WHERE h.idMascota.id = :idMascota").setParameter("idMascota", id);
        consultaCita.setParameter("idMascota", id);
        List<EntradaHistorial> citas = consultaCita.getResultList();
        return citas;
    }
    
    // Métode que retorna la llista de cites medicas que te el veterinari que es troba dins del sistema.
    private List consultaLlistaCitaVeterinari(Usuari veterinari, EntityManager em) {
    
        int id = veterinari.getId();
        Query consultaCita = em.createQuery("SELECT h FROM Cita h WHERE h.idVeterinari.id = :idVeterinari").setParameter("idVeterinari", id);
        consultaCita.setParameter("idVeterinari", id);
        List<EntradaHistorial> citas = consultaCita.getResultList();
        return citas;
    }
    
    // Métode que imprimeix en pantalla del client que ha accedit al servidor la llista de citas pendents. 
    private void llitaCitas(List<Cita> citas ,PrintWriter writer) {
        writer.println("Llista de citas:");
        for (Cita cita : citas) {
            writer.println("- " + cita.getData() + " " + cita.getDescripcio());
        }
    }
    
    // Métode per editar les dades d'una cita médica, ha d'arribar l'id de la cita a editar y les dades actualitzades.
    private void editaCita(String[] reg, EntityManagerFactory emf, PrintWriter writer) {
        
        int idCita = Integer.parseInt(reg[2]);
        
        Cita editaCita = new CitaJpaController(emf).findCita(idCita);
        editaCita.setData(Date.from(LocalDate.parse(reg[3]).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        editaCita.setDescripcio(reg[4]);
        
        try {
            new CitaJpaController(emf).edit(editaCita);
            writer.println("update_success");
        } catch (Exception e) {
        }
    }
    
    // Métode per eliminar una cita médica, a d'arribar l'id de la cita a eliminar.
    private void borraCita(String[] reg, EntityManagerFactory emf, PrintWriter writer) throws NonexistentEntityException{
        
        int id = Integer.parseInt(reg[2]);
        new CitaJpaController(emf).destroy(id);
        writer.println("delete_success");
    }
}