package main.java.car.tp2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ConnectException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;
import org.apache.commons.net.ftp.parser.DefaultFTPFileEntryParserFactory;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.net.ftp.parser.ParserInitializationException;

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
		conf.setServerLanguageCode(FTPClientConfig.SYST_UNIX);

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

	public FTPFile[] listFiles(String folder_path){
		FTPFile[] ftpFiles = null;
		try {
			authenticate();
			if(!isAuthenticated()){
				return null;
			}
			if(folder_path != null)
				ftpFiles = client.listFiles(folder_path);
			else
				ftpFiles = client.listFiles();
			
			client.disconnect();
		} catch(Exception e){
			System.err.println("Error from here");
			e.printStackTrace();
		}
		return ftpFiles;
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


	public void storeFile(InputStream is, String path){
		try {
			authenticate();
			if(!isAuthenticated()){
				return;
			}

			client.storeFile(path, is);
		} catch(Exception e){
			System.err.println("Error from here");
			e.printStackTrace();
		}

	}
	
	
	public void deleteFile(String filepath){
		try {
			authenticate();
			if(!isAuthenticated()){
				return;
			}

			client.deleteFile(filepath);
		} catch(Exception e){
			System.err.println("Error from here <egwrxh");
			e.printStackTrace();
		}

	}
}
