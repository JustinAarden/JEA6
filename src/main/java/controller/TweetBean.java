/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package controller;

import domain.Tweet;
import domain.User;
import service.KService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Justin on 3-3-2016.
 */
@Named(value="TweetBean")
@RequestScoped
public class TweetBean {

    @Inject
    KService kwetterService;


    private Long userId;

    private  String result;

    private Collection<Tweet> tweetcol;

    private  String tweetText;


    private String get = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get( "id" );
    Long  id = new Long(get);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



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



public void addTweet() {

    get = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get( "id" );
    Long  userid = new Long(get);
    User user = kwetterService.find(userid);

    if (user != null) {
            user.addTweet(new Tweet(tweetText, new Date(),"JSF Tweet"));
            kwetterService.edit(user);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml?id=" + userid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml?id=" + userid +"&error=error");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



    public Collection<Tweet> getTweetcol() {
        return tweetcol;
    }

    public void getTweetCol(){
        User user = kwetterService.find(id);
        tweetcol =  user.getTweets();

    }


}
