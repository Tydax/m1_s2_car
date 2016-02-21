package tp1.src;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import org.mockito.Mockito;


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
		assertEquals(2048, s.getLocalPort());
		assertEquals(System.getProperty("user.dir"), s.getFolderPath()); /* Intestable en dur, selon l'endroit d'où on teste */
	}

	@Test
	public void testUser(){
		s.addUser("azerty", "qwerty");
		assertTrue(s.users.containsKey("azerty"));
		assertEquals("qwerty", s.users.get("azerty"));


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
		final String PASSWORD = "qwerty";
		s.addUser(USERNAME, PASSWORD);

		try {
			/* Client socket */
			Socket soc = new Socket("127.0.0.1", 2048);

			FtpRequest ftp = new FtpRequest(s, soc, s.getFolderPath());
			String msg1 = ftp.processUSER(USERNAME);
			String msg2 = ftp.processPASS(PASSWORD);

			assertEquals("331 Waiting for user password...", msg1);
			assertEquals("230 Authentication successful", msg2);
			assertTrue(ftp.isAuthenticated());


		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthentificationFailedBecauseThereIsNoUsernameProvided(){

		final String USERNAME = "azerty";
		final String PASSWORD = "qwerty";
		s.addUser(USERNAME, PASSWORD);

		try {
			/* Client socket */
			Socket soc = new Socket("127.0.0.1", 2048);

			FtpRequest ftp = new FtpRequest(s, soc, s.getFolderPath());
			String msg2 = ftp.processPASS(PASSWORD);

			assertEquals("430 No username provided", msg2);
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

			FtpRequest ftp = new FtpRequest(s, soc, s.getFolderPath());
			String msg1 = ftp.processUSER(WRONG_USERNAME);

			assertEquals("430 Authentication failed", msg1);
			assertFalse(ftp.isAuthenticated());
		} catch (final Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthentificationFailedBecauseWrongPassword(){

		final String USERNAME = "azerty";
		final String PASSWORD = "qwerty";
		final String WRONG_PASSWORD = "azert";
		s.addUser(USERNAME, PASSWORD);

		try {
			/* Client socket */
			Socket soc = new Socket("127.0.0.1", 2048);

			FtpRequest ftp = new FtpRequest(s, soc, s.getFolderPath());
			String msg1 = ftp.processUSER(USERNAME);
			String msg2 = ftp.processPASS(WRONG_PASSWORD);

			assertEquals("331 Waiting for user password...", msg1);
			assertEquals("430 Authentication failed", msg2);


		} catch (final Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testSendingFile() throws Exception {

		final String USERNAME = "azerty";
		final String PASSWORD = "qwerty";
		s.addUser(USERNAME, PASSWORD);

		final String msg1;

        /* Client socket */
        final Socket soc = new Socket("127.0.0.1", 2048);

        final FtpRequest ftp = new FtpRequest(s, soc, s.getFolderPath());
        ftp.processUSER(USERNAME);
        ftp.processPASS(PASSWORD);
        ftp.processPORT("127,0,0,1,189,145");

        msg1 = ftp.processRETR(".classpath");
        assertEquals("425 Unable to complete the transfer", msg1);
	}

	@Test
	public void testReceivingFile() throws Exception {
		final String username = "azerty";
		final String password = "qwerty";
        s.addUser(username, password);

        final String msg1;

        final Socket socket = new Socket("127.0.0.1", 2048);
        final FtpRequest ftp = new FtpRequest(s, socket, s.getFolderPath());
        ftp.processUSER(username);
        ftp.processPASS(password);
        ftp.processPORT("127,0,0,1,189,145");

        msg1 = ftp.processSTOR(".classpath");
        assertEquals("425 Unable to complete the transfer", msg1);
	}

    @Test
    public void testListFiles() throws Exception {
        final String username = "azerty";
        final String password = "qwerty";
        s.addUser(username, password);

        final String msg1;
        final Socket socket = new Socket("127.0.0.1", 2048);
        final FtpRequest ftp = new FtpRequest(s, socket, s.getFolderPath());
        ftp.processUSER(username);
        ftp.processPASS(password);
        ftp.processPORT("127,0,0,1,189,145");
        msg1 = ftp.processLIST();

        assertEquals("250", msg1);
    }

    @Test
    public void testChangeDirectory() throws Exception {
        final String username = "azerty";
        final String password = "qwerty";
        s.addUser(username, password);

        final String msg1;
        final Socket socket = new Socket("127.0.0.1", 2048);
        final FtpRequest ftp = new FtpRequest(s, socket, s.getFolderPath());
        ftp.processUSER(username);
        ftp.processPASS(password);
        ftp.processPORT("127,0,0,1,189,145");
        final String formerPath = ftp.getWorkingDirectory().getAbsolutePath();
        final StringBuilder newPath = new StringBuilder();
        newPath.append(formerPath);
        newPath.append("/");
        newPath.append("src");
        msg1 = ftp.processCWD("src");

        assertEquals("250 " + ftp.getWorkingDirectory().getAbsolutePath(), msg1);
        assertEquals(newPath.toString(), ftp.getWorkingDirectory().getAbsolutePath());
    }

    @Test
    public void testChangeDirectoryError() throws Exception {
        final String username = "azerty";
        final String password = "qwerty";
        s.addUser(username, password);

        final String msg1;
        final Socket socket = new Socket("127.0.0.1", 2048);
        final FtpRequest ftp = new FtpRequest(s, socket, s.getFolderPath());
        ftp.processUSER(username);
        ftp.processPASS(password);
        ftp.processPORT("127,0,0,1,189,145");
        final String formerPath = ftp.getWorkingDirectory().getAbsolutePath();
        msg1 = ftp.processCWD("thegame"); // Folder that does not exist

        assertEquals("530 Folder does not exist", msg1);
        assertEquals(formerPath, ftp.getWorkingDirectory().getAbsolutePath());
    }

    @Test
    public void testChangeToParentDirectory() throws Exception {
        final String username = "azerty";
        final String password = "qwerty";
        s.addUser(username, password);

        final String msg1;
        final Socket socket = new Socket("127.0.0.1", 2048);
        final FtpRequest ftp = new FtpRequest(s, socket, s.getFolderPath());
        ftp.processUSER(username);
        ftp.processPASS(password);
        ftp.processPORT("127,0,0,1,189,145");
        final String formerPath = ftp.getWorkingDirectory().getAbsolutePath();
        final StringBuilder newPath = new StringBuilder();
        newPath.append(formerPath);
        newPath.append("/");
        newPath.append("..");
        msg1 = ftp.processCDUP();

        assertEquals("250 " + ftp.getWorkingDirectory().getAbsolutePath(), msg1);
        assertEquals(newPath.toString(), ftp.getWorkingDirectory().getAbsolutePath());
    }

    @Test
    public void testPrintDirectory() throws Exception {
        final String username = "azerty";
        final String password = "qwerty";
        s.addUser(username, password);

        final String msg1;
        final Socket socket = new Socket("127.0.0.1", 2048);
        final FtpRequest ftp = new FtpRequest(s, socket, s.getFolderPath());
        ftp.processUSER(username);
        ftp.processPASS(password);
        ftp.processPORT("127,0,0,1,189,145");
        msg1 = ftp.processPWD();
        final String path = ftp.getWorkingDirectory().getAbsolutePath();

        assertEquals("250 " + path, msg1);
    }

	@Test
	public void testDeconnexion() throws Exception {
        final String username = "azerty";
        final String password = "qwerty";
        s.addUser(username, password);

        final String msg1;
        final Socket socket = new Socket("127.0.0.1", 2048);
        final FtpRequest ftp = new FtpRequest(s, socket, s.getFolderPath());
        ftp.processUSER(username);
        ftp.processPASS(password);
        ftp.processPORT("127,0,0,1,189,145");

        msg1 = ftp.processQUIT();
        assertEquals("221 Goodbye! Stay safe out there!", msg1);
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
