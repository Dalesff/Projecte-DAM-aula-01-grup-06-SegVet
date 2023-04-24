package m13_ales_f_server;

import m13_ales_f_server.controller.Server;

/**
 * 
 * @author David Ales Fernandez
 */
public class M13_Ales_F_Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Server server = new Server();
        server.start();
    }
}
