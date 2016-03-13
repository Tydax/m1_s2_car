package main.java.car.tp2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ConnectException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class PasserelleServer {

	private FTPClient client;
	private String host;
	private int port;
	private String username;
	private String password;

	public PasserelleServer(String host, int port, String username, String password){
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;

		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);

		client = new FTPClient();
		client.configure(conf);
		//client.set
	}

	private void authenticate(){

		try {
			client.connect(host, port);

			//client.enterLocalPassiveMode();
			client.login(username, password);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private boolean isAuthenticated(){
		return client.isConnected();
	}

	public File retrieve(String path){
		File file = null;

		try {
			authenticate();
			if(!isAuthenticated()){
				throw new ConnectException("User is not authenticated");
			}
			String [] pathArray = path.split("/");
			String filename = pathArray[pathArray.length - 1];

			file = new File(filename);
			FileOutputStream fos = new FileOutputStream(file);

			client.retrieveFile(path, fos);
			fos.close();

			client.disconnect();
		} catch(Exception e){
			e.printStackTrace();
		}

		return file;
	}

	public String[] listFiles(){
		String[] filenames = null;
		FTPFile[] ftpFiles = null;
		try {
			authenticate();
			if(!isAuthenticated()){
				return null;
			}
			ftpFiles = client.listFiles();
			filenames = new String[ftpFiles.length];
			System.out.println("Length : " + ftpFiles.length);

			for(int i=0;i<ftpFiles.length;i++){
				filenames[i] = ftpFiles[i].getName();
			}

			client.disconnect();
		} catch(Exception e){
			System.err.println("Error from here");
			e.printStackTrace();
		}
		return filenames;
	}


	public String getWorkingDir(){

		try {
			authenticate();
			if(!isAuthenticated()){
				return null;
			}

			return client.printWorkingDirectory();
		} catch(Exception e){
			System.err.println("Error from here");
			e.printStackTrace();
		}
		return "";

	}


	public String storeFile(InputStream is, String path){

		try {
			authenticate();
			if(!isAuthenticated()){
				return null;
			}

			client.storeFile(path, is);
		} catch(Exception e){
			System.err.println("Error from here");
			e.printStackTrace();
		}
		return "";

	}
}
