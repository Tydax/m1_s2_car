package src.main.java.car.tp2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.html.HTML;
import javax.ws.rs.Consumes;
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

	final static private String cmdUrl = "http://localhost:8080/rest/tp2/commandes";

	@GET
	@Produces("application/octet-stream")
	@Path("/get/{path}")
	public File get(@PathParam("path") String path ) {
		PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);

		File file = null;
		try {
			file = ps.retrieve(path);
		} catch (final Exception e){
			e.printStackTrace();
		}
		return file;
	}

	@GET
	@Produces("text/html")
	@Path("/list{folder_path : (/folder_path)?}")
	public String list(@PathParam("folder_path") String folderPath ) {
		final PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);


		FTPFile[] ftpFiles = null;
		try {
			ftpFiles = ps.listFiles(folderPath);
		} catch (final Exception e){
			e.printStackTrace();
		}
		if (ftpFiles == null){
			return "";
		}



		if (folderPath == null || folderPath.equals("null")) {
			folderPath = "";
		}

        final String[] listContent = new String[ftpFiles.length];
		for (int i = 0; i < listContent.length; i++) {
            listContent[i] = HTMLGenerator.generateLinkFromFile(cmdUrl, folderPath, ftpFiles[i]);
        }

        final String list = HTMLGenerator.generateList(listContent);
		return HTMLGenerator.generatePage(list + HTMLGenerator.generateUploadForm());
	}


	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/html; charset=UTF-8")
	@Path("/upload")
	public String upload( @Multipart("file") InputStream fichier, @Multipart("name") String name, @Multipart("path") String path) throws IOException {
		PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);

		ps.storeFile(fichier, path + "/" + name);
		
		System.out.println("RQDTNE : " + path + "/" + name);

		return HTMLGenerator.generatePage("<h2>Fichier importé avec succès</h2><a href='http://localhost:8080/rest/tp2/commandes/list'>Retour</a>");
	}


	@GET
	@Produces("text/html; charset=UTF-8")
	@Path("/delete/{filepath}")
	public String deleteFile(@PathParam("filepath") String filepath ){
		PasserelleServer ps = new PasserelleServer(Constants.host, Constants.port, Constants.username, Constants.password);

		ps.deleteFile(filepath);
		
		
		return HTMLGenerator.generatePage("<h3>Fichier supprimé avec succès</h3>");
		/*
		Client client;
		WebResource webResource = client.resource("http://localhost:8080/RestApp/sample/employees");
		String input = "{\"name\":\"John\"}";
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class,input);
		
		*/
	}
}

