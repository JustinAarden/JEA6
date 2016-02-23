
package rest;

import domain.Tweet;
import domain.User;
import service.KService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.List;


@Path("/rest")
//@RequestScoped
public class KwetterResource {

    @Context
    private UriInfo context;


   // @Inject
   // KService kwetterService;
    final KService kwetterService = KService.instance();

//WAAROM WERKT INJECT NIET?? Error occurred during deployment: Exception while loading the app : EJB Container initialization error. intellIj
    public KwetterResource() {
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("api")
    public List<User> findAllUsers() {
        return kwetterService.findAll();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("api/count")
    public int count() {
        return kwetterService.count();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("api/{userID}")
    public User findUser(@PathParam("userID") Long id) {
        return kwetterService.find(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addtweet/{user1}/{message}")
    public User testMethod(@PathParam("user1") Long id, @PathParam("message") String message)  {
        Date date;
        User user = kwetterService.find(id);
        user.addTweet(new Tweet(message, new Date(),"test" ,kwetterService.nextTweetID()));
        return kwetterService.find(id);
   }


}
