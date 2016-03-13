package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.junit.Test;
import ftpserver.FTPServer;

public class FTPServerTest 
{
	private String server_dir = "/Users/Ax/Desktop/Test";
	
	@Test
	/**
	 * Test the source folder given to the server
	 */
	public void testGetServerDir()
	{
		FTPServer s = new FTPServer(this.server_dir,1027);
		assertTrue(s.getServerDir().equals(this.server_dir+"/"));	
		s = null;
	}
	
	@Test
	/**
	 * Test if a client can connect and disconnect from the server with given address and port
	 */
	public void testClientCmdConnectionDisconnection()
	{
		FTPServer server = new FTPServer(this.server_dir,1048);
		server.start();
		
		Socket s;
		
		try 
		{
			s = new Socket("localhost", 1048);
			assertTrue(s.isConnected());
			assertTrue(new BufferedReader(new InputStreamReader(s.getInputStream())).readLine().substring(0, 3).equals("220"));
			s.close();
			assertTrue(s.isClosed());
		} 
		catch (IOException e) 
		{
			assertTrue(false);
		}
		server.stop_listening();
		server = null;
	}
	
	@Test
	/**
	 * Test if the server can add a new user and its password
	 */
	public void testAddUserPass()
	{
		FTPServer server = new FTPServer(this.server_dir,1049);
		server.addUserPass("John", "Doe");
		
		assertTrue(server.userExists("John"));
		assertTrue(server.checkUserPass("John", "Doe"));
		
		server = null;

	}
	
	@Test
	/**
	 * Test if the server is able to check user/password combinason
	 */
	public void testCheckuserPass()
	{
		FTPServer server = new FTPServer(this.server_dir,1050);
		server.addUserPass("John", "Doe");
		assertTrue(server.checkUserPass("John", "Doe"));
		
		server = null;
	}
	
	@Test
	/**
	 * test if the server is able to check if a user exists
	 */
	public void testUserExists()
	{
		FTPServer server = new FTPServer(this.server_dir,1051);
		server.addUserPass("John", "Doe");
		assertTrue(server.userExists("John"));
		server = null;
	}
	
		
}
