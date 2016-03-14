package main.java.car.tp2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.net.ftp.FTPFile;
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

	final private String form_upload = "<div><h1 style='font-size:1.2em; font-family: sans'>Telecharger un fichier</h1><form method='POST' action='http://localhost:8080/rest/tp2/commandes/upload' enctype='multipart/form-data'>\n" +
			"Choisir le fichier<input type='file' name='file'><br> nom de la destination : <input type='hidden' name='path' value='#'><input type='text' name='name' /><br />\n" +
			"<input type='submit' value='Telecharger'>\n" +
			"</form> </div>";
	
	final private String header = "<!doctype html><html lang='fr'><head><meta charset='utf-8'><title>Serveur FTP</title></head><body>";
	final private String footer = "</body></html>";

	final private String url = "http://localhost:8080/rest/tp2/commandes";

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
	@Path("/list{folder_path : (/folder_path)?}")
	public String list(@PathParam("folder_path") String folder_path ) {
		PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);


		FTPFile[] ftpFiles = null;
		try {
			ftpFiles = ps.listFiles(folder_path);

		} catch(Exception e){
			e.printStackTrace();
		}
		if(ftpFiles == null){
			return "";
		}
		String html = "<ul>";


		if(folder_path == null || folder_path.equals("null"))
			folder_path = "";

		for(FTPFile file : ftpFiles){
			String download_link = "";
			String delete_link = "";
			if(file.isDirectory()){
				download_link = url + "/list/" + folder_path + file.getName();
				delete_link = "";
			} else {
				download_link = url + "/get/" + folder_path + file.getName();
				delete_link = "<a href='" + url + "/delete/" + folder_path + file.getName() + "'>Delete</a>";

				
			}
			html += "<li><a href='" + download_link + "'>" + file.getName() +  delete_link + "</li>";
		}
		html += "</ul>";
		html += form_upload.replace("#", folder_path);
		return header + html + footer;
	}


	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/html; charset=UTF-8")
	@Path("/upload")
	public String upload( @Multipart("file") InputStream fichier, @Multipart("name") String name, @Multipart("path") String path) throws IOException {
		PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);


		ps.storeFile(fichier, path + "/" + name);
		
		System.out.println("RQDTNE : " + path + "/" + name);

		return header + "<h2>Fichier importé avec succès</h2><a href='http://localhost:8080/rest/tp2/commandes/list'>Retour</a>" + footer;
	}


	@GET
	@Produces("text/html; charset=UTF-8")
	@Path("/delete/{filepath}")
	public String deleteFile(@PathParam("filepath") String filepath ){
		PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);

		ps.deleteFile(filepath);
		
		
		return header + "<h3>Fichier supprimé avec succès</h3>" + footer;
		/*
		Client client;
		WebResource webResource = client.resource("http://localhost:8080/RestApp/sample/employees");
		String input = "{\"name\":\"John\"}";
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class,input);
		
		*/
	}
}

