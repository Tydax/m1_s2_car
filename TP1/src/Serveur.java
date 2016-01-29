import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Serveur extends ServerSocket{
	
	 public static final Map<String, String> users = new HashMap<String, String>();
	
	 /**
	 * Constructor for the class to represent a server
	 * for the FTP Client Server interaction
	 */
	public Serveur(int port, String folderPath) throws IOException {
		super(port);
		
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
			System.out.println("Waiting for connexion with a client");
			Socket socket = accept();
			
			System.out.println("Connexion etablished");
			
			socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//FtpRequest ftpRequest = new FtpRequest(in.readLine());
		    //ftpRequest.start();
		}
	}
		
	
	public static void main(String [] args) throws IOException{
		Serveur.addUser("azerty", "azerty");
		Serveur s = new Serveur(2048, "");
		s.run();
	}



}
