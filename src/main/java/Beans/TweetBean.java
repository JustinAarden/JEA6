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
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Justin on 3-3-2016.
 */
//@Named(value="TweetBean")
@ManagedBean(name = "TweetBean")
@ViewScoped
public class TweetBean implements Serializable {

    @Inject
    KService kwetterService;



    private ArrayList<Tweet> allTweets = new ArrayList<>();



    private ArrayList<Tweet> mentions = new ArrayList<>();
    private ArrayList<Tweet> tweetsBySingleUser = new ArrayList<>();
    private  String tweetText;
    private String latestTweet;
    private Date latestTweetDAte;
    private User user = new User();
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();



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

    public void addTweet(){
            kwetterService.addTweet(user,tweetText,"JSF");
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
    public void gotToErrorPage() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml?error=nouser");}


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

        if(!isNullOrBlank(request.getParameter("id"))){
                Long id = Long.parseLong(request.getParameter("id"));
            user = kwetterService.find(id);
            mentions.addAll(user.getMentions());
            for (Tweet tweets : user.getTweets()) {
                tweetsBySingleUser.add(tweets);
                allTweets.add(tweets);
                setlatestTweet();
            }
            if(isNullOrBlank(request.getParameter("tweets"))){
               tweetsByFollowing();
            }

        }else{
            try {
                gotToErrorPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
