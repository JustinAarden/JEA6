
package service.rest;

import domain.Tweet;
import domain.User;
import service.KService;

import javax.faces.bean.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@Path("")
@RequestScoped
public class KwetterResource {

    @Context
    private UriInfo context;


    KService kwetterService;

    /**
     * Creates a new instance of KwetterResource
     */
    public KwetterResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("api")
    public List<User> findAllUsers() {
        return kwetterService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("api/{userID}")
    public String addTweet(Tweet tweet, @PathParam("userID") Long userID) {
        Boolean succes;
        String message = "";
        
        tweet.setId(kwetterService.nextTweetID());
        succes = kwetterService.find(userID).addTweet(tweet);
        if (!succes) {
            message = "Error adding tweet";
        }

        return String.format("{\"succes\":\"%b\",\"message\":\"%s\"}", succes, message);
    }
}
