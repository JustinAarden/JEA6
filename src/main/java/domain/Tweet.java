/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
@Entity

public class Tweet implements Serializable, Comparable<Tweet> {
    private static final long serialVersionUID = 1L;
    private static long nextID = 0L;

    @Id @GeneratedValue
    private Long id;
    private String tweetText;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateField")
    private Date date;
    private String postedFrom;


    @ManyToMany
    @JoinTable(
            name = "mentions",
            joinColumns = @JoinColumn(name = "tweet_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"
            )
    )
    private List<User> mentioned = new ArrayList<>();



    public Tweet() {
    }

    public Tweet(String tweet) {
        this.tweetText = tweet;
    }

    public Tweet(String tweetText, Date datum, String vanaf) {
        this.tweetText = tweetText;
        this.date = datum;
        this.postedFrom = vanaf;
    }

    @XmlElement(required = true)
    public Long getId() {
        return id;
    }
    @XmlElement(required = true)
    public String getTweetText() {
        return tweetText;
    }
    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }
    @XmlElement(required = true)
    public Date getDatum() {
        return date;
    }
    public void setDatum(Date datum) {
        this.date = datum;
    }
    @XmlElement(required = true)
    public String getVanaf() {
        return postedFrom;
    }

    public void setVanaf(String vanaf) {
        this.postedFrom = vanaf;
    }


    public List<User> getMentioned() {
        return mentioned;
    }

    public void setMentioned(User mentioned) {
        this.mentioned.add(mentioned);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tweetText != null ? tweetText.hashCode() + date.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tweet)) {
            return false;
        }
        Tweet other = (Tweet) object;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public String toString() {
        return "kwetter.domain.Tweet[id=" + date.toString() + "]";
    }

    @Override
    public int compareTo(Tweet t) {
        return getDatum().compareTo(t.getDatum());
    }
}
