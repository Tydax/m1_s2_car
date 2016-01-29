import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestFtpRequest {
	
	Serveur s;
	
	@Before
	public void initServer(){
		try {
			Serveur.addUser("azerty", "azerty");
			s = new Serveur(2048, "");
			s.run();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testConnexion(){
		
	}
	
	@Test
	public void testAuthentificationSuccess(){
		
	}
	
	@Test
	public void testAuthentificationFailed(){
		
	}
	
	@Test
	public void testSendingFile(){
		
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
