package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@Entity
public class Tweet implements Serializable {

    private static final long serialVersionUID = 1L;
    private static long nextID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tweetText;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateField")
    private Date date;
    private String postedFrom;

    public Tweet() {
    }

    public Tweet(String tweet) {
        this.tweetText = tweet;
        //this.id = User.nextID++;
        //only used for UserDAOCollectionImpl JPA uses @GeneratedValue
    }

    public Tweet(String tweetText, Date datum, String vanaf) {
        this.tweetText = tweetText;
        this.date = datum;
        this.postedFrom = vanaf;
        //this.id = Tweet.nextID++;
        //only used for UserDAOCollectionImpl, JPA uses @GeneratedValue
    }
    @XmlElement(required = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
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
        return "twitter.domain.Tweet[id=" + date.toString() + "]";
    }

}
