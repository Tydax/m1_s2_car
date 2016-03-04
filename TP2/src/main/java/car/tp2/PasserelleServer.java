package main.java.car.tp2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.net.ftp.FTPClient;
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
        
        client = new FTPClient();
    }
    
    private void authenticate() throws IOException{
        System.out.println("Blabla");
        client.connect(host, port);
        
        
        client.login(username, password);
        System.out.print(client.getReplyString());
    }
    
    private boolean isAuthenticated(){
        return client.isConnected();
    }
    
    public File retrieve(String path) throws IOException{
        System.out.println("Start retrieve");
        authenticate();
        if(!isAuthenticated()){
            throw new ConnectException("User is not authenticated");
        }
        String [] pathArray = path.split("/");
        String filename = pathArray[pathArray.length - 1];
        
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        
        client.retrieveFile(path, fos);
        fos.close();
        
        client.logout();
        
        return file;
    }
    
    public String[] listFiles() throws IOException{
        System.out.println("Start retrieve");
        authenticate();
        if(!isAuthenticated()){
            throw new ConnectException("User is not authenticated");
        }
       
        
        FTPFile[] ftpFiles = client.listFiles();
        for(FTPFile file : ftpFiles){
            System.out.println(file.getName());
        }
        
        client.logout();
        
        return null;
    }

}
