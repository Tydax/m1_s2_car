package main.java.car.tp2;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.net.ftp.FTPClient;

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
        
        FTPClient client = new FTPClient();
    }
    
    private void authenticate() throws IOException{
        client.connect(host, port);
        client.user(username);
        client.pass(password);
    }
    
    private boolean isAuthenticated(){
        return client.isConnected();
    }
    
    public File retrieve(String path) throws IOException{
        authenticate();
        if(!isAuthenticated()){
            throw new ConnectException("User is not authenticated");
        }
        
        File file = new File();
        
        client.retrieveFile(remote, local)
        
        
        return null;
    }

}
