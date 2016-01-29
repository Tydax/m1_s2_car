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
	// 220 dès qu'un client a demandé et réussi une co (donc avant même l'authentification)
	// 226 transfert réalisé avec succès
	// 221 deconnexion
	// 230 authentification réussi
	// 331 user reconnnu. en attente de mot de passe
	// 430 identification ou mdp incorrect
	// 500 erreur de syntaxe. commande non reconnue et action non effectée
	// 501 erreur de syntaxe dans les paramètres ou les arguments

	public static final Map<String, String> users = new HashMap<String, String>();
	
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
			System.out.println("Waiting for connexion with a client");
			Socket socket = accept();

			PrintWriter out = new PrintWriter(socket.getOutputStream(), 
		            true);
			out.print(Constants.CODE_CONNECTION_SUCC + " OK\n");
			out.flush();
			System.out.println("Connexion etablished");

			socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			FtpRequest ftpRequest = new FtpRequest(socket, folderPath, in.readLine());
			ftpRequest.start();

		}
	}


	public static void main(String [] args){
		try {
			Serveur.addUser("azerty", "azerty");
			Serveur s = new Serveur(2048, "");
			s.run();

			s.close();
		} catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}



}
