
/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package rest;

import domain.Tweet;
import domain.User;
import service.KService;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/rest")
//@RequestScoped
@Stateless
public class KwetterResource {



   @Inject KService kwetterService;



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
    @Path("getuseronname/{username}")
    public User findOnName(@PathParam("username") String name){return kwetterService.find(name);}


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


    @POST
    @Path("api/login")

    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @Context HttpServletRequest request) {
        try {
            request.getSession(); //make sure a session has started
            request.login(username, password);
        } catch (Exception ex) {
            Logger.getLogger(KwetterResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().entity(ex.getLocalizedMessage()).build();
        }
        return Response.ok("main.html?id=" + kwetterService.find(username).getId()).build();
    }

    @GET
    @Path("api/logout")
    //@RolesAllowed({"user_role", "admin_role"})
    public Response logout(@Context HttpServletRequest request) {
        try {
            request.logout();
            request.getSession().invalidate();
        } catch (ServletException ex) {
            Logger.getLogger(KwetterResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().entity(ex.getLocalizedMessage()).build();
        }
        return Response.ok("logged out").build();
    }

    @GET
    @Path("api/isLoggedIn/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isLoggedIn(@PathParam("id") Long id, @Context HttpServletRequest request) {
        String username = request.getRemoteUser();
        if (username == null) {
            return Response.serverError().entity("{\"jms\":\"No user is logged in\"}").build();
        }
        User user = kwetterService.find(username);
        if (Objects.equals(user.getId(), id)) {
            return Response.ok("{\"jms\":\"Ok\"}").build();
        } else {
            return Response.serverError().entity("{\"jms\":\"Other user is logged in\",\"id\":\"" + user.getId() + "\"}").build();
        }
    }

    @GET
    @Path("api/startbatch")
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
    public User testMethod(@PathParam("user1") Long id, @PathParam("jms") String message)  {
        User user = kwetterService.find(id);
        user.addTweet(new Tweet(message, new Date(),"REST-API"));
        return kwetterService.find(id);
    }



}
