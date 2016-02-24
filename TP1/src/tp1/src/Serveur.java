package tp1.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class Serveur extends ServerSocket {

    public final Map<String, String> users = new HashMap<>();

    private String folderPath;

    /**
     * Constructor for the class to represent a server
     * for the FTP Client Server interaction
     */
    public Serveur(int port, String folderPath) throws IOException {
        super(port);
        this.folderPath = folderPath;

    }

    public void addUser(final String user, final String password){
        users.put(user, password);
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

            final FtpRequest ftpRequest = new FtpRequest(this, socket, getFolderPath());
            ftpRequest.start();
        }
    }
    
    public String getFolderPath(){
    	return folderPath;
    }
    
    public static void main(String [] args){
        try {    		
            final Serveur s = new Serveur(2048, System.getProperty("user.dir"));
            s.addUser("azerty", "qwerty");

            s.run();

            s.close();
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
