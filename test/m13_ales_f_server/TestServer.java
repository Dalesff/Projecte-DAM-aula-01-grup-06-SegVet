package m13_ales_f_server;



import java.io.IOException;
import java.io.PrintWriter;
import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import org.junit.Test;
import java.sql.*;

/**
 * 
 * @author David Ales Fernandez
 */

public class TestServer {
    
    /**
     * Test que evalua la conexió a la base de dades.
     * @throws SQLException 
     */
    @Test
    public void dbConnection() throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SegVet", "postgres", "grup6");
        
        assertNotNull(conn);
    }
    
    /**
     * Test que evalua que el mètode validateCredencials() valida correctament les credencials d'un usuari.
     * @throws SQLException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    @Test
    public void testValidateCredencials() throws SQLException, SecurityException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        
        Server testServer = new Server();
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SegVet", "postgres", "grup6");

        Method validateCredencialsMethod = Server.class.getDeclaredMethod("validateCredencials", String.class, String.class, Connection.class);
        validateCredencialsMethod.setAccessible(true);

        boolean resultat = (boolean) validateCredencialsMethod.invoke(testServer, "pepe@segvet.com", "123", connection);

        assertTrue(resultat);
    }
    
    /**
     * Test que evalua que el mètode validateCredencials() valida correctament les credencials d'un usuari.
     * @throws SQLException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    @Test
    public void testBadCredencials() throws SQLException, SecurityException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        
        Server testServer = new Server();
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SegVet", "postgres", "grup6");

        Method validateCredencialsMethod = Server.class.getDeclaredMethod("validateCredencials", String.class, String.class, Connection.class);
        validateCredencialsMethod.setAccessible(true);

        boolean result = (boolean) validateCredencialsMethod.invoke(testServer, "pepe@segvet.com", "666", connection);

        assertFalse(result);
    }
    
    /**
     * Test que evalua si es registra adecuadament les sessions actives.
     * @throws IOException 
     */
    @Test
    public void testSessioActiva() throws IOException {
        
        Server testServer = new Server();
        Socket socket = new Socket("localhost", 44556);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("login" + ":" + "pepe@segvet.com" + ":" + "123");
        assertEquals(true, testServer.getActiveSessions().getOrDefault("pepe@segvet.com", Boolean.TRUE));
        out.println("logout");
        socket.close();
    }
    
    /**
     * Test que evalua que s'eliminan del registre adecuadament les sessions tancades.
     * @throws IOException 
     */
    @Test
    public void testSessioTancada() throws IOException {
        
        Server testServer = new Server();
        Socket socket = new Socket("localhost", 44556);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("login" + ":" + "paco@segvet.com" + ":" + "456");
        out.println("logout");
        assertEquals(false, testServer.getActiveSessions().getOrDefault("paco@segvet.com", Boolean.FALSE));
        socket.close();
    }
}



