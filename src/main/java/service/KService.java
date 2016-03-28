

/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package service;

import dao.TweetDao;
import dao.UserDao;
import domain.Tweet;
import domain.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//@ApplicationScoped
@Stateless
@Named
public class KService {

    /**
     * This is a Injection of the userDao
     * which is a interface of UserDAO_JPAImpl
     */
    @Inject
    UserDao userdao;
    @Inject
    TweetDao tweetDao;

    /**
     *This method creates a user and persist it in a database
     *
     * @param user
     */
    public void create(User user) {
        userdao.create(user);
    }

    /**
     * This method edits a user and merge it in a database
     * With this method you can update the properties
     * add a follower
     * After this you merge te user with the existing one
     *
     * @param user
     */
    public void edit(User user) {
        userdao.edit(user);
    }

    /**
     *  This method removes a user and persist it in a database
     * @param user
     */
    public void remove(User user) {
        userdao.remove(user);
    } //TODO not implemented correctly, no priority atm

    /**
     *This method returns all users
     * @return
     */
    public List<User> findAll() {
        return userdao.findAll();
    }

    /**
     *
     * This method finds the user on his or her ID
     * @param id
     * @return
     */
    public User find(Object id) {
        return userdao.find((Long)id);
    }

    /**
     *This method finds the user based on the username
     * @param name
     * @return
     */
    public User find(String name){
        return userdao.find(name);
    } //find on name

    /**
     *
     * This method counts the total users
     * @return
     */


    public List<User>  findFollowing(Object id) {
        return userdao.findFollowing((Long)id);
    }

/*
    public List<User>  findFollower(Object id) { return userdao.findFollower((Long)id);
    }
*/

    /**
     * Get a list of tweets where the user is mentioned based on the user id
     * @param id
     * @return
     */
    public List<Tweet> getMentionedTweets(Object id) {
        return tweetDao.getMentionedTweets((Long)id);
    }

    /**
         @ matches the character @ literally
            (?<username>[A-Za-z][A-Za-z0-9_-]*) Named capturing group username
            [A-Za-z0-9_-]* match a single character present in the list below
                A-Z a single character in the range between A and Z (case sensitive)
                a-z a single character in the range between a and z (case sensitive)
                0-9 a single character in the range between 0 and 9
                _- a single character in the list _- literally
     */
    public final static Pattern REGEXGROUP = Pattern.compile("@(?<user>[A-Za-z0-9_-]*)");

    /**
     *
     *THis method is to add a tweet
     * When adding a tweet it checks if it can finds a username
     * the username always starts with a "@"
     * @param user this is the user that sends the tweet
     * @param tweetText this is the text to be tweeted
     * @return
     */
    public void addTweet(User user, String tweetText, String location) {
        Tweet tweet;
        if(location.isEmpty()){
            tweet = user.addTweet(tweetText, "TESTlocation",user); //TODO SET LOCATION AS VARIABLE
        }else {
            tweet = user.addTweet(tweetText, location, user); //TODO SET LOCATION AS VARIABLE
        }

            //Checks for mentions in the tweet
            Matcher regexmatcher = REGEXGROUP.matcher(tweetText);
            while (regexmatcher.find()) {
                User mentionedUser = userdao.find(regexmatcher.group("user"));

                if (mentionedUser == null) {
                    continue;
                }
                //mentionedUser.addMention(tweetText, "TEST");
                tweet.setMentioned(mentionedUser);
            }

    }



    public int count() {
        return userdao.count();
    }

    /**
     *
     * Still not used
     * @return
     */
    public Long nextTweetID(){
        return userdao.nextTweetID();
    }
}