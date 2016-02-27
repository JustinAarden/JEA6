
package rest;

import domain.Tweet;
import domain.User;
import interceptors.Tweetinterceptor;
import service.KService;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.Properties;


@Path("/rest")
//@RequestScoped
@Stateless
public class KwetterResource {



   @Inject KService kwetterService;
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

/*    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getuseronname/{username}")
    public User findOnName(@PathParam("username") String name) {
        User user = new User();
        return kwetterService.fin(id);
    }*/

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addfollower/{user1}/{user2}")
    public String addFollower(@PathParam("user1") Long id, @PathParam("user2") Long id2)  {
        User user = kwetterService.find(id);
        User user2 = kwetterService.find(id2);
        String followers = "";
        String following ="";
        if(user.getName() !=null || user2.getName() != null){
            if(user.getFollowing().contains(user2.getId())){
                return "Already following that user!";
            }else if(user.getId().equals(user2.getId())){
                return "You can't follow yourself :-)";
            }else{
                user.addFollowing(id2);
                user2.addFollower(id);
            }
        }else{
            return "Either User 1 or User 2 doesn't exist!";
        }

        for (Long forId : user.getFollowers())
        {
            followers += kwetterService.find(forId).getName();

        }
        for (Long forId : user.getFollowing())
        {
            following += kwetterService.find(forId).getName();

        }


        return user.getName() + " ==>   Followed:   ==>   " + user2.getName() +     System.lineSeparator() + " And is already Following  " +  following +  System.lineSeparator() + " And is followed by  " +  followers;
    }


    @GET
    @Path("api/startBatch")
    @Produces(MediaType.TEXT_PLAIN)
    //@RolesAllowed("admin_role")
    public String startBatch() {
        JobOperator jo = BatchRuntime.getJobOperator();
        long jid = jo.start("kwetterJson", new Properties());
        return "Job submitted: " + jid;
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
