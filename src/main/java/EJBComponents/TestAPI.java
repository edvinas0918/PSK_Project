package EJBComponents;
import com.sun.net.httpserver.HttpServer;
//import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import java.io.IOException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Dziugas on 4/2/2016.
 */
@Stateless
@Path("/helloworld")
public class TestAPI {

    @PersistenceContext(unitName = "NameliaiPersistenceUnit")
    private EntityManager em;

    @GET
    @Produces("text/plain")
    public String getClichedMessage() {
        // Return some cliched textual content
        return "Hello World";
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/save")
    public void createNewComment(){
        CommentsEntity comment = new CommentsEntity();
        comment.setAddress("Api street");
        comment.setSummary("This one was created with an API");
        comment.setTestytest("API test");
        em.persist(comment);
    }

}
