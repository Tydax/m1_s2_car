package main.java.car.tp2;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Exemple de ressource REST accessible a l'adresse :
 * 
 *      http://localhost:8080/rest/tp2/helloworld
 * 
 * @author Lionel Seinturier <Lionel.Seinturier@univ-lille1.fr>
 */
@Path("/commandes")
public class TP2Resource {

    @GET
    @Produces("application/octet-stream")
    @Path("/{path}")
    public File get(@PathParam("path") String path ) {
        File file = new File(path);
        
        return file;
    }
}

