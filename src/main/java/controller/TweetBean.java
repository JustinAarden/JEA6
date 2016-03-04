/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package controller;

import domain.Tweet;
import domain.User;
import service.KService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Justin on 3-3-2016.
 */
//@Named(value="TweetBean")
@ManagedBean(name = "TweetBean")
@ViewScoped
public class TweetBean implements Serializable {

    @Inject
    KService kwetterService;



    private  String result;
    private ArrayList<Tweet> tweetcol = new ArrayList<>();
    private Long userId;
    private  String tweetText;


 /*   private String get = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get( "id" ).replaceAll("\\s+","");
    private Long  id = new Long(get);*/

    private static Long testid;


    private User user = new User();

    public String getTweetText() {
        return tweetText;
    }
    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getUserId() {
        return userId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }



public void addTweet(){
            user.addTweet(new Tweet(tweetText, new Date(),"JSF Tweet"));
            kwetterService.edit(user);
        try {
            goToMainPageByUserID(user.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
 }



    public void goToMainPageByUserID(Long id) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml?id=" + id +"&success=1");

    }
    public Collection<Tweet> getTweetcol() {
        return tweetcol;
    }

    public void init(Long id){
           user = kwetterService.find(id);
        //tweetcol = (List<Tweet>) user.getTweets();
        for (Tweet tweets : user.getTweets()) {
            tweetcol.add(tweets);

        }
        if(!user.getFollowing().isEmpty()){
            for (Long forid :user.getFollowing()) {
                User found = kwetterService.find(forid);
                tweetcol.addAll(found.getTweets());
                }

            }
        }



}
