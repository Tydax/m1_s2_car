package main.java.car.tp2;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Exemple de ressource REST accessible a l'adresse :
 * 
 *      http://localhost:8080/rest/tp2/commandes
 * 
 * @author Lionel Seinturier <Lionel.Seinturier@univ-lille1.fr>
 */
@Path("/commandes")
public class TP2Resource {

    @GET
    @Produces("application/octet-stream")
    @Path("/{path}")
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
}

