package ftpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class of the server. It listens clients from the port chosen and handle the connection in the FTPClientHandler class. 
 * 
 * @author Axel Antoine <ax.antoine@gmail.com> <axel.antoine@etudiant.univ-lille1.fr>
 *
 */
public class FTPServer extends Thread
{
	/**
	 * HashMap containing the user (String) and their password (String)
	 */
	private HashMap<String,String> user_pass_list;
	
	/**
	 * Main ServerSocket of the server
	 */
	private ServerSocket serverSocket;
	
	/**
	 * The source directory of users Home folders
	 */
	private String server_dir;
	
	/**
	 * While listening is true, the server is listenning on the port for new clients
	 * False value will stop the stop by the call of stop function
	 */
	private boolean listening = true;
	
	private ArrayList<FTPClientHandler> clients_list;
	
	/**
	 * Create a FTP server, start it with the source directory and the port.
	 * @param server_dir, the source directory of users Home folders
	 * @param port, the port of the ftp server
	 */
	public FTPServer(String server_dir, int port)
	{
		this.user_pass_list = new HashMap<String,String>();
		this.clients_list = new ArrayList<FTPClientHandler>();
		this.server_dir = server_dir;
		if(!this.server_dir.endsWith("/"))
			this.server_dir+="/";
		
		// Open the server socket to listen on the port
		try 
		{
			this.serverSocket = new ServerSocket(port);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the server source directory containing users home folders
	 * @return the source directory under a string
	 */
	public String getServerDir()
	{
		return this.server_dir;
	}
	
	/**
	 * Add a user to the server
	 * @param user
	 * @param pass
	 */
	public void addUserPass(String user, String pass)
	{
		this.user_pass_list.put(user, pass);
	}
	
	/**
	 * Check if the password pass is equal to the password registered in the server
	 * @param user
	 * @param pass, the password to check
	 * @return true if password is ok, false otherwise
	 */
	public synchronized boolean checkUserPass(String user, String pass)
	{
		if(this.user_pass_list.containsKey(user))
		{
			if(this.user_pass_list.get(user).equals(pass))
				return true;
		}
		return false;
	}
	
	/**
	 * Check if the user exists in the database of the server
	 * @param user
	 * @return true if the user exists, false otherwise
	 */
	public boolean userExists(String user)
	{
		return this.user_pass_list.containsKey(user);
	}
	
	/**
	 * Start the server
	 */
	public void run()
	{
		System.out.println("Server listening...");
		Socket s;
		FTPClientHandler client_handler;
		while(this.listening)
		{
			try 
			{
				s = this.serverSocket.accept();
				client_handler = new FTPClientHandler(s,this);
				this.clients_list.add(client_handler);
				client_handler.start();
			} 
			catch (IOException e) 
			{
				
			}
		}
	}
	
	/**
	 * Stops the server
	 */
	public void stop_listening()
	{
		this.listening = false;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FTPClientHandler getClient(int idx)
	{
		return this.clients_list.get(idx);
	}
	
	public void removeClient(FTPClientHandler client)
	{
		this.clients_list.remove(client);
	}
	
	public int getNbClients()
	{
		return this.clients_list.size();
	}
}
