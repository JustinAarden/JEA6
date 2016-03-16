/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

@XmlRootElement
@Entity
@NamedQueries({
        @NamedQuery(name = "User.count",
                query = "select count(user) from User user"),
        @NamedQuery(name = "User.findAll",
                query = "select user from User user"),
        @NamedQuery(name = "User.findID",
                query = "select User from User user where user.id =:id"),
        @NamedQuery(name = "User.findName",
                query = "select User from User user where user.name =:name"),
        @NamedQuery(name = "User.findByFollowing",
                query = "select User from User user join user.followers f where f.id=:id"),
        @NamedQuery(name = "User.findFollower",
                query = "select User, f from User user join user.followers f where user.id=:id")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private static long nextID = 0L;

    @Id @GeneratedValue//(strategy = GenerationType.IDENTITY)
    private Long id; //AI
    private String name;
    private String web;
    private String image = "http://www.dravenstales.ch/wp-content/uploads/2011/02/fb_storm.jpg";
    private String bio;
    private String location = "Nederland";

    private String password;
    @ManyToMany
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "name", referencedColumnName = "name"), inverseJoinColumns = @JoinColumn(name = "roleID", referencedColumnName = "roleID"))
    private List<Role> roles = new ArrayList();

    @SuppressWarnings("JpaAttributeTypeInspection")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> followers = new ArrayList();

    @ManyToMany(mappedBy = "mentioned")
    private List<Tweet> mentions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tweet> tweets = new ArrayList();

    public User() {
    }

    public User(String naam) {
        this.name = naam;
   }

    public User(String naam, String web, String bio) {
        this.name = naam;
        this.web = web;
        this.bio = bio;}



    /*
    *
    * Getters and setters
    *
    *===================SECTION=================
    *
    *====================USER===================
    * */
    public void setPassword(String password) {
        this.password = password;
    }
    public void addGroup(Role role) {
        this.roles.add(role);
    }

    @XmlElement(required = true)
    public List<Role> getGroup() {
        return this.roles;
    }

    @XmlElement(required = true)
    public Long getId() {
        return id;
    }

    @XmlElement(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(required = true)
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @XmlElement(required = true)
    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @XmlElement(required = true)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public List<Tweet> getMentions() {
        return mentions;
    }

    public void setMentions(List<Tweet> mentions) {
        this.mentions = mentions;
    }


        /*
    *
    * Getters and setters
    *
    *===================SECTION=================
    *
    *====================Tweets===================
    * */

    @XmlElement(required = true)
    public Collection<Tweet> getTweets() {
        return Collections.unmodifiableCollection(tweets);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }




    /*
    *===================SECTION=================
    *
    *==================FOLLOWERS================
    *
    * */
    @XmlElement(required = true)
    public Collection<User> getFollowers() {

        return Collections.unmodifiableCollection(followers);


    }

    public void addTweet(Tweet tweet) {
        this.tweets.add(tweet);
    }


    public Tweet addTweet(String content, String location) {
        Tweet tweet = new Tweet(content, new Date(), location);
        addTweet(tweet);
        return tweet;
    }


    public Boolean addFollower(User follower){
        return followers.add(follower);
    }

    /*=====================================================================================================
    * =====================================================================================================*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() + bio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return this.hashCode() == other.hashCode();
    }


    @Override
    public String toString() {
        return "kwetter.domain.User[naam=" + name + "]";
    }

    public void removeTweet(Tweet tweet) {
        tweets.remove(tweet);
    }
}