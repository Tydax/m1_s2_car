package main;

import ftpserver.FTPServer;

/**
 * 
 * @author Axel Antoine <ax.antoine@gmail.com> <axel.antoine@etudiant.univ-lille1.fr>
 *
 */
public class Main 
{
	public static void main(String[] args)
	{
		// Create and start the ftp Server
		FTPServer s = new FTPServer(".",1027);
		
		// Add Some Random Users to Test the Server : :
		s.addUserPass("azerty", "qwerty");
		s.addUserPass("Tata", "mdpTata");
		s.addUserPass("Titi", "mdpTiti");
		
		// Start the server :
		s.start();
	}
}
