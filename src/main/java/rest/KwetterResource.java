
/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package rest;

import domain.Role;
import domain.Tweet;
import domain.User;
import interceptors.Tweetinterceptor;
import service.KService;
import websocket.kwettersocket;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/rest")
@RequestScoped
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
    @Path("users/{userID}")
    public String getUser(@PathParam("userID") Long id) {
        User user = kwetterService.find(id);
        return this.JsonIfy(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("users")
    public String getAllUsers() {
       int totalusers = kwetterService.count();
        int counter = 1;
        Long userid = 1L;
        String userlist = "[";
        while (counter <= totalusers){
            if(counter == totalusers){
                userlist += this.JsonIfy(kwetterService.find(userid));
            }else{
                userlist += this.JsonIfy(kwetterService.find(userid))+ ",";
            }
            userid++;
            counter++;
        }
        userlist +="]";
        return userlist;

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("users/{userID}")
    @RolesAllowed("user_role")
    public String addTweet(Tweet tweet, @PathParam("userID") Long userID) {
        //TODO: USER ID CAN BE REPLACED BY request.getRemoteUser() FOR THE USERNAME OF LOGGED IN USER
        Boolean succes;
        String message = "";

        User user = kwetterService.find(userID);
        //We need to use new Tweet() here so the id can be given in the contructor
        kwetterService.addTweet(user,tweet.getTweetText(),"WEB");
        kwetterService.edit(user);


        kwettersocket.send("new tweet");

        return String.format("{\"succes\":\"%b\",\"message\":\"%s\"}", message);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("gettweetsofuser/{userID}")
    public String getTweetsofUser(@PathParam("userID") Long id) {
        User user = kwetterService.find(id);
        Collection<Tweet> tweets = user.getTweets();
        String jsontweets = "[";
        for (Tweet tweet: tweets
             ) {
                  jsontweets += tweet.toJSON();

        }
        jsontweets+="]";
        return jsontweets;

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user/{userID}")
    public String getEverythingOfUser(@PathParam("userID") Long id) {
        User user = kwetterService.find(id);
        return this.JsonIfy(user);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getTweets/{userID}")
    public Collection<Tweet> getTweets(@PathParam("userID") Long id) {
        User user = kwetterService.find(id);
        return user.getTweets();

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getuseronname/{username}")
    public User findOnName(@PathParam("username") String name){return kwetterService.find(name);}


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addfollower/{user1}/{user2}")
    public void addFollower(@PathParam("user1") Long id, @PathParam("user2") Long id2)  {
        User user = kwetterService.find(id);
        User user2 = kwetterService.find(id2);
        String followers = "";
        String following ="";
        if(user.getName() !=null || user2.getName() != null){
            if(!user2.getFollowers().contains(user)){
                user2.addFollower(user);
            }else{
               /// return "already following this user";
            }
        }else{
           // return "Either User 1 or User 2 doesn't exist!";
        }

        for (User followinguser: kwetterService.findFollowing(user.getId())
                ) { following += followinguser.getName();

        }
        for (User followinguser: user.getFollowers()
                ) { followers += followinguser.getName();

        }
        kwetterService.socketNewFollower();
        //return user.getName() + " ==>   Followed:   ==>   " + user2.getName() +     System.lineSeparator() + " And is already Following  "  + following +  System.lineSeparator() + " And is followed by  " +  followers;
    }
    @GET
    @Path("removeFollower/{user1}/{user2}")
    public void removeFollower(@PathParam("user1") Long id, @PathParam("user2") Long id2)  {
        User user = kwetterService.find(id);
        User user2 = kwetterService.find(id2);
        String returnstring = "";
        String following ="";
        if(user.getName() !=null || user2.getName() != null){
            if(user.getFollowers().contains(user2)){
                kwetterService.removeFollower(user,user2);
                returnstring = "OK!";
            }
     }

        kwetterService.socketNewFollower();

        //return JsonIfy(user);
        //return user.getName() + " ==>   Followed:   ==>   " + user2.getName() +     System.lineSeparator() + " And is already Following  "  + following +  System.lineSeparator() + " And is followed by  " +  followers;
    }





    @POST
    @Path("api/login")
    @PermitAll
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @Context HttpServletRequest request) {
        try {
            request.getSession(); //make sure a session has started
            request.login(username, password);
            Logger.getGlobal().log(Level.SEVERE,username + password);
        } catch (Exception ex) {
            Logger.getLogger(KwetterResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().entity(ex.getLocalizedMessage()).build();
        }
        return Response.ok("main.html?id=" + kwetterService.find(username).getId()).build();
    }

    @GET
    @Path("api/logout")
    @RolesAllowed({"user_role", "admin_role"})
    public Response logout(@Context HttpServletRequest request) {
        try {
            request.logout();
            request.getSession().invalidate();
        } catch (ServletException ex) {
            Logger.getGlobal().log(Level.SEVERE,"Logout Failed");
            Logger.getLogger(KwetterResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().entity(ex.getLocalizedMessage()).build();
        }
        Logger.getGlobal().log(Level.SEVERE,"Logout correct");
        return Response.ok("index.html?loggedout=true").build();
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("addtweet/{user1}/{message}")
    @RolesAllowed("user_role")
    @Interceptors(Tweetinterceptor.class)
    public void addTweet(@PathParam("user1") Long id, @PathParam("message") String message,  @Context HttpServletRequest request)  {
        User user = kwetterService.find(id);
        kwetterService.addTweet(user,message,"REST");
        kwetterService.socketNewTweet();

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("init")
    public void initUsers(){
        kwetterService.initUsers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("api/getmentions/{user1}" )
    public String getMentions(@PathParam("user1") Long id){
        User user = kwetterService.find(id);
        String mentions = "";
        if(kwetterService.getMentionedTweets(user.getId()).isEmpty()){
            mentions += "there are no mentions";
        }else{
           mentions +=  user.getMentions().toString();
        }
       return  mentions;
    }
    @GET
    @Path("api/currentRole")
    @PermitAll
    public String getCurrentRole(@Context HttpServletRequest request) {
        request.getSession();
        String[] allRoles = {"user_role", "admin_role"};
        List userRoles = new ArrayList(allRoles.length);
        for (String role : allRoles) {
            if (request.isUserInRole(role)) {
                userRoles.add(role);
            }
        }
        return Arrays.toString(userRoles.toArray());
    }

    @GET
    @Path("api/isAdmin")
    @RolesAllowed("admin_role")
    public String isAdmin() {
        return "Current user is logged in as admin";
    }

    @GET
    @Path("api/isUser")
    @RolesAllowed("user_role")
    public String isUser() {
        return "Current user is logged in as user";
    }



    public  String JsonIfy(User user) {
        int counter = 1;
        String tweet = "";
        String followers = "";
        String following = "";
        String group = "";
        String roles = "";

        for(Tweet tweets : user.getTweets()){
            if(user.getTweets().size() == counter){
                tweet += tweets.toJSON();
                counter = 1;
            }else{
                tweet += tweets.toJSON()+",";
            }
            counter++;
        }
        counter = 1;

        for(User follower : user.getFollowers()){
            if(user.getFollowers().size() == counter){
                followers += follower.getId();
                counter = 1;
            }else{
                followers += follower.getId()+",";
            }
            counter++;
        }
        counter = 1;

        for (User followinguser: kwetterService.findFollowing(user.getId())
             ) {
            if(kwetterService.findFollowing(user.getId()).size() == counter){
                following += followinguser.getId();
                counter=1;
            }else{
                following += followinguser.getId()+",";
            }
            counter++;
        }
        counter = 1;
        for (Role role: user.getRoles()) {
            if(user.getRoles().size() == counter){
                group += role.groupsToJson();
                roles += role.rolesToJson();
                counter=1;
            }else{
                group += role.groupsToJson()+",";
                roles += role.rolesToJson()+",";
            }
            counter++;
        }

        return   "{"
                + "\"id\":" + user.getId().intValue() + ",\n "
                + "\"name\":\"" + user.getName() + "\",\n "
                + "\"web\":\"" + user.getWeb() + "\",\n "
                + "\"image\":\"" + user.getImage() + "\",\n "
                + "\"bio\":\"" + user.getBio() + "\", "
                + "\"location\":\"" + user.getLocation() + "\",\n "
                + "\"tweets\":[" + tweet + "],\n"
                + "\"followers\":[" + followers + "],\n"
                + "\"following\":[" + following + "],\n"
                + "\"group\":[" + group + "],\n"
                + "\"roles\":[" + roles + "]\n"
                + "}\n\n";
    }


}
