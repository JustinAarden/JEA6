
package rest;

import domain.Tweet;
import domain.User;
import interceptors.Tweetinterceptor;
import service.KService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;


@Path("/rest")
//@RequestScoped
@Stateless
public class KwetterResource {



   @Inject
    KService kwetterService;
    //final KService kwetterService = KService.instance();

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
    @Interceptors(Tweetinterceptor.class)
    public User testMethod(@PathParam("user1") Long id, @PathParam("message") String message)  {
        User user = kwetterService.find(id);
        user.addTweet(new Tweet(message, new Date(),"REST-API"));
        return kwetterService.find(id);
   }


}
