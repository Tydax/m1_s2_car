package main.java.car.tp2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

/**
 * Exemple de ressource REST accessible a l'adresse :
 * 
 *      http://localhost:8080/rest/tp2/commandes
 * 
 * @author Lionel Seinturier <Lionel.Seinturier@univ-lille1.fr>
 */
@Path("/commandes")
public class TP2Resource {
	
	final private String form_upload = "<div><h1 style='font-size:1.2em; font-family: sans'>Téléverser un fichier</h1><form method='POST' action='http://localhost:8080/rest/tp2/commandes/upload' enctype='multipart/form-data'>\n" +
            "Choisir le fichier<input type='file' name='file'><br> nom de la destination : <input type='text' name='name' /><br />\n" +
            "<input type='submit' value='Téléverser'>\n" +
            "</form> </div>" + 
            "<div><h1 style='font-size:1.2em; font-family: sans'>Supprimer un fichier</h1><form method='POST' action='http://localhost:8080/rest/tp2/commandes/delete'><input type='text' name='name' />" + 
            "<input type='submit' value='Delete'></form></div>";

    @GET
    @Produces("application/octet-stream")
    @Path("/get/{path}")
    public File get(@PathParam("path") String path ) {
        PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);
        
        File file = null;
        try {
            file = ps.retrieve(path);
        } catch(Exception e){
            e.printStackTrace();
        }
        return file;
    }
    
    @GET
    @Produces("text/html")
    @Path("/list")
    public String list() {
        PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);
        
        String[] filenames = new String[0];
        String workingDir = "";
        try {
        	filenames = ps.listFiles();
        	workingDir = ps.getWorkingDir();
        } catch(Exception e){
            e.printStackTrace();
        }
        if(filenames == null){
        	return "";
        }
        String html = "<ul>";
        for(String filename : filenames){
        	html += "<li><a href='http://localhost:8080/rest/tp2/commandes/get" + workingDir + filename + "'>" + filename + "</li>";
        }
        html += "</ul>";
        html += form_upload;
        return html;
    }
    
    
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/html; charset=UTF-8")
	@Path("/upload")
	public String upload( @Multipart("file") InputStream fichier, @Multipart("name") String name) throws IOException {
        PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);
        
        System.out.println("String : " + name);
        
        ps.storeFile(fichier, name);
        
        return "<h2>Fichier importé avec succès</h2><a href='http://localhost:8080/rest/tp2/commandes/list'>Retour</a>";
    }
}

