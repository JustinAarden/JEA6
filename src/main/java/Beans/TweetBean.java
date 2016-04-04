/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package Beans;

import domain.Tweet;
import domain.User;
import service.KService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Justin on 3-3-2016.
 */
//@Named(value="TweetBean")
@ManagedBean(name = "TweetBean")
@ViewScoped
public class TweetBean implements Serializable {

    @Inject
    KService kwetterService;

    @Inject
    HttpServletRequest request;
    @Inject
    HttpSession session;

    private ArrayList<Tweet> allTweets = new ArrayList<>();



    private ArrayList<Tweet> mentions = new ArrayList<>();
    private ArrayList<Tweet> tweetsBySingleUser = new ArrayList<>();
    private  String tweetText;
    private String latestTweet;
    private Date latestTweetDAte;
    private User user = new User();
    private NavigationBean navigationBean =new NavigationBean();


    public String getTweetText() {
        return tweetText;
    }
    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public ArrayList<Tweet> getTweetsBySingleUser() {
        return tweetsBySingleUser;
    }
    public Date getLatestTweetDAte() {
        return latestTweetDAte;
    }
    public String getLatestTweet() {
        return latestTweet;
    }
    public ArrayList<Tweet> getMentions() {
        return mentions;
    }

    public String getUserPrincipalName() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Principal principal = fc.getExternalContext().getUserPrincipal();
        if(principal == null) {
            return null;
        }
        return principal.getName();
    }


    public void addTweet(){
            kwetterService.addTweet(user,tweetText,"JSF");
            kwetterService.edit(user);
        try {
            navigationBean.goToIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void addMentions(){
        mentions.addAll(kwetterService.getMentionedTweets(user.getId()));
    }

    public Collection<Tweet> getAllTweets() {
        Collections.sort(allTweets);
        Collections.reverse(allTweets);
        return allTweets;
    }

    public void setlatestTweet(){
        int latest =  tweetsBySingleUser.size() -1;
        latestTweet = tweetsBySingleUser.get(latest).getTweetText();
        latestTweetDAte = tweetsBySingleUser.get(latest).getDatum();
    }


    public void tweetsByFollowing(){
            if(!kwetterService.findFollowing(user.getId()).isEmpty()){
                for (User followinguser: kwetterService.findFollowing(user.getId())){
                    allTweets.addAll(followinguser.getTweets());
                }
            }
    }

    /**
     *
     * @param s
     * @return
     */
    private boolean isNullOrBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }


    public void init(){

        user = kwetterService.find(getUserPrincipalName());

        Logger.getGlobal().log(Level.SEVERE,"tweetbean USER: " + user.getName() + user.getPassword());
            for (Tweet tweets : user.getTweets()) {
                tweetsBySingleUser.add(tweets);
                allTweets.add(tweets);
                setlatestTweet();
            }
            if(isNullOrBlank(request.getParameter("tweets"))){
               tweetsByFollowing();
            }
        addMentions();
        Logger.getGlobal().log(Level.SEVERE,"mentions: " + user.getMentions().toString());
/*
        if(!isNullOrBlank(request.getParameter("id"))){
            user=kwetterService.find(request.getParameter("id"));
        }else{}
*/

        }

}






