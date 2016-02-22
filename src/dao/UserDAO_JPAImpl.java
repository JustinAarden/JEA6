/*
package dao;

*/
/**
 * Created by Justin on 22-2-2016.
 *//*


import domain.Tweet;
import domain.User;
import domain.Role;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Alternative
@Stateless
public class UserDAO_JPAImpl implements UserDao {

    private Role user = new Role("user_role");
    private Role admin = new Role("admin_role");

    @PersistenceContext
    private EntityManager em;

    public UserDAO_JPAImpl() {
    }

    @PostConstruct
    private void initUsers() {
        User u1 = new User("Hans", "http", "geboren 1");
        User u2 = new User("Frank", "httpF", "geboren 2");
        User u3 = new User("Tom", "httpT", "geboren 3");
        User u4 = new User("Sjaak", "httpS", "geboren 4");

        this.create(u1);
        this.create(u2);
        this.create(u3);
        this.create(u4);

        addFollower(u1, u2);
        addFollower(u1, u3);
        addFollower(u1, u4);
        addFollower(u2, u1);
        addFollower(u3, u1);
        addFollower(u4, u1);

        Tweet t1 = new Tweet("Hallo", new Date(), "PC");
        Tweet t2 = new Tweet("Hallo again", new Date(), "PC");
        Tweet t3 = new Tweet("Hallo where are you", new Date(), "PC");
        u1.addTweet(t1);
        u1.addTweet(t2);
        u1.addTweet(t3);

        em.persist(user);
        em.persist(admin);

        u1.addGroup(user);
        u1.addGroup(admin);
        u2.addGroup(user);
        u3.addGroup(user);
        u4.addGroup(user);

        this.edit(u1);
        this.edit(u2);
        this.edit(u3);
        this.edit(u4);
    }

    @Override
    public int count() {
        Query q = em.createNamedQuery("User.count");
        return Integer.parseInt(q.getSingleResult() + "");
    }

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public void edit(User user) {
        em.merge(user);
    }

    @Override
    public List<User> findAll() {
        Query q = em.createNamedQuery("User.findAll");
        return q.getResultList();
    }

    @Override
    public User find(Long id) {
        Query q = em.createNamedQuery("User.findID");
        q.setParameter("id", id);
        return (User) q.getSingleResult();
    }

    @Override
    public User find(String name) {
        Query q = em.createNamedQuery("User.findName");
        q.setParameter("name", name);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void remove(Tweet tweetToRemove) {
        User user = null;
        for (User u : findAll()) {
            for (Tweet t : u.getTweets()) {
                if (t.getId() == tweetToRemove.getId()) {
                    user = u;
                    tweetToRemove = t;
                    break;
                }
            }
        }
        if (user != null) {
            user.removeTweet(tweetToRemove);
        }
        em.remove(em.find(Tweet.class, tweetToRemove.getId()));
    }

    @Override
    public void remove(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addFollower(User userToFollow, User follower) {
        userToFollow.addFollower(follower.getId());
        follower.addFollowing(userToFollow.getId());
    }

    @Override
    public Long nextTweetID() {
        //Beter zou het gebruik van @GeneratedValue bij id hier zijn
        List<User> users = findAll();
        Long nextID = 0L;
        for (User u : users) {
            for (Tweet t : u.getTweets()) {
                if (t.getId() >= nextID) {
                    nextID = t.getId() + 1;
                }
            }
        }
        return nextID;
    }

}*/
