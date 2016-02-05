package tp1.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Serveur extends ServerSocket{

    public static final Map<String, String> users = new HashMap<>();

    public String folderPath;

    /**
     * Constructor for the class to represent a server
     * for the FTP Client Server interaction
     */
    public Serveur(int port, String folderPath) throws IOException {
        super(port);
        this.folderPath = folderPath;

    }

    public static void addUser(String user, String password){
        Serveur.users.put(user, password);
    }

    /**
     * Main method for the Server class
     * @throws IOException
     */
    public void run() throws IOException{
        while(true){
            System.out.println("[Server] Waiting for connexion with a client...");
            Socket socket = accept();

            final PrintWriter out = new PrintWriter(socket.getOutputStream(),
                    true);
            final String msg = String.format(Constants.MSG_CONNECTION_SUCC, Constants.CODE_CONNECTION_SUCC);
            out.println(msg);
            out.flush();

            System.out.println("[Server] Connection established");

            socket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            final FtpRequest ftpRequest = new FtpRequest(socket, folderPath);
            ftpRequest.start();
        }
    }


    public static void main(String [] args){
        try {
            Serveur.addUser("azerty", "azerty");
            Serveur s = new Serveur(2048, System.getProperty("user.dir"));
            s.run();

            s.close();
        } catch(final Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }
}
