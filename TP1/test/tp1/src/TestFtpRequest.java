package tp1.src;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class TestFtpRequest {

	Serveur s;

	@Before
	public void initServer(){
		try {
			s = new Serveur(2048, System.getProperty("user.dir"));
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	@Test
	public void testInitialisation(){
		assertEquals(s.getLocalPort(), 2048);
		assertEquals(s.getFolderPath(), System.getProperty("user.dir")); /* Intestable en dur, selon l'endroit d'où on teste */
	}

	@Test
	public void testUser(){
		s.addUser("azerty", "azerty");
		assertTrue(s.users.containsKey("azerty"));
		assertEquals(s.users.get("azerty"), "azerty");


		/**
		MyClass test = Mockito.mock(MyClass.class);

		// define return value for method getUniqueId()
		when(test.getUniqueId()).thenReturn(43);

		// use mock in test.... 
		assertEquals(test.getUniqueId(), 43);
		 */
	}

	@Test
	public void testAuthentificationSuccess(){
		final String USERNAME = "azerty";
		final String PASSWORD = "azerty";
		s.addUser(USERNAME, PASSWORD);

		try {
			/* Client socket */
			Socket soc = new Socket("127.0.0.1", 2048);

			FtpRequest ftp = new FtpRequest(s, soc, s.folderPath);
			String msg1 = ftp.processUSER(USERNAME);
			String msg2 = ftp.processPASS(PASSWORD);

			assertEquals(msg1, "331 Waiting for user password...");
			assertEquals(msg2, "230 Authentication successful");
			assertTrue(ftp.isAuthenticated());


		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthentificationFailedBecauseThereIsNoUsernameProvided(){

		final String USERNAME = "azerty";
		final String PASSWORD = "azerty";
		s.addUser(USERNAME, PASSWORD);

		try {
			/* Client socket */
			Socket soc = new Socket("127.0.0.1", 2048);

			FtpRequest ftp = new FtpRequest(s, soc, s.folderPath);
			String msg2 = ftp.processPASS(PASSWORD);

			assertEquals(msg2, "430 No username provided");
			assertFalse(ftp.isAuthenticated());


		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthentificationFailedBecauseWrongUsername(){

		final String USERNAME = "azerty";
		final String WRONG_USERNAME = "azert";
		s.addUser(USERNAME, "");

		try {
			/* Client socket */
			Socket soc = new Socket("127.0.0.1", 2048);

			FtpRequest ftp = new FtpRequest(s, soc, s.folderPath);
			String msg1 = ftp.processUSER(WRONG_USERNAME);

			assertEquals(msg1, "430 Authentication failed");
			assertFalse(ftp.isAuthenticated());





		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthentificationFailedBecauseWrongPassword(){

		final String USERNAME = "azerty";
		final String PASSWORD = "azerty";
		final String WRONG_PASSWORD = "azert";
		s.addUser(USERNAME, PASSWORD);

		try {
			/* Client socket */
			Socket soc = new Socket("127.0.0.1", 2048);

			FtpRequest ftp = new FtpRequest(s, soc, s.folderPath);
			String msg1 = ftp.processUSER(USERNAME);
			String msg2 = ftp.processPASS(WRONG_PASSWORD);

			assertEquals(msg1, "331 Waiting for user password...");
			assertEquals(msg2, "430 Authentication failed");


		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testSendingFile() throws UnknownHostException, IOException{

		final String USERNAME = "azerty";
		final String PASSWORD = "azerty";
		s.addUser(USERNAME, PASSWORD);

		String msg1 = null;

		try {
			/* Client socket */
			Socket soc = new Socket("127.0.0.1", 2048);

			FtpRequest ftp = new FtpRequest(s, soc, s.folderPath);
			ftp.processUSER(USERNAME);
			ftp.processPASS(PASSWORD);
			ftp.processPORT("127,0,0,1,189,145");

			msg1 = ftp.processRETR(".classpath");
			assertEquals(msg1, "425 Unable to complete the transfert");

		} catch(Exception e){

		}

	}

	@Test
	public void testReceivingFile(){

	}

	@Test
	public void testDeconnexion(){

	}

	@After
	public void closeServer(){
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
