package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.junit.Test;

import ftpserver.FTPClientHandler;
import ftpserver.FTPServer;

public class FTPClientHandlerTest 
{
	private String server_dir = "/Users/Ax/Desktop/Test";
	
	// The functions to dialog with a with are in FTPClientHandler class
	// FTPServer stores an ArrayList of handlers, here we test the functions
	
	@Test
	/**
	 * This is the test if the function to write strings on the Cmd Socket with the client
	 */
	public void testWriteClient()
	{
		FTPServer server = new FTPServer(this.server_dir,1052);
		server.start();
		
		Socket s;
		FTPClientHandler c;
		try 
		{
			s = new Socket("localhost", 1052);
			
			// Verify connexion with the server : (handled in FTPServer class)
			assertTrue(s.isConnected());
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			assertTrue(r.readLine().substring(0, 3).equals("220"));

			// Get the instance of the FtpClientHandler in the server, here were are the only client, get the idx 0
			try {
				wait(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c = server.getClient(0);
			
			String str = "This is a string.";
			c.writeClient(str);
			
			
			assertTrue(r.readLine().equals(str));
						
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
	public void testProcessUSER()
	{
		FTPServer server = new FTPServer(this.server_dir,1052);
		String user = "IAMUSER";
		String pass = "PASSWORD";
		server.addUserPass(user,pass);
		server.start();
		
		Socket s;
		String line;
		try 
		{
			s = new Socket("localhost", 1052);
			
			// Verify connexion with the server : (handled in FTPServer class)
			assertTrue(s.isConnected());
			BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			line = reader.readLine();
			System.out.println("line"+line);
			assertTrue(line.substring(0, 3).equals("220"));

			// Get the instance of the FtpClientHandler in the server, here were are the only client, get the idx 0
			
			String cmd = "USER "+user;
			// Write the cmd on the soket
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			writer.write(cmd);
			writer.flush();
			System.out.println("hello");
			String return_code = "331";
					
			// Test the answer which should begin by the code 331
			//line = reader.readLine();
			System.out.println("line"+line);
			assertTrue(line.substring(0, 3).equals(return_code));
						
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
}
