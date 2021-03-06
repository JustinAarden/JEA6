/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package dao;


import domain.Role;
import domain.Tweet;
import domain.User;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Local(UserDao.class)
@Stateless
public class UserDAO_JPAImpl implements UserDao {


    @PersistenceContext
    private EntityManager em;



    public UserDAO_JPAImpl() {

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
      //  em.clear();
        Query q = em.createNamedQuery("User.findID");
        q.setParameter("id", id);
        return (User) q.getSingleResult();
    }

   @Override
    public User find(String name) {
    //   em.clear();
        Query q = em.createNamedQuery("User.findName");
        q.setParameter("name", name);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<User> findFollowing(Long id) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query q = em.createNamedQuery("User.findByFollowing");
        q.setParameter("id", id);
        try {
           return q.getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }
    @Override
    public List<User> findFollower(Long id) {
        em.getEntityManagerFactory().getCache().evictAll();
       // Query q = em.createQuery("SELECT u from User u where u.id=(Select f.id from u.followers f where f.id=:id)");
        Query q = em.createQuery("SELECT u from User u inner join u.followers f where u.id=:id");
        //Query q = em.createNamedQuery("User.findFollower");
        q.setParameter("id", id);
        List<User> userlist = q.getResultList();
        try {
            return userlist;
        } catch (NoResultException ex) {
            return null;
        }
    }


    public void removeFollower(User user, User usertoremove) {
        Query query = em.createNativeQuery("DELETE FROM user_user where User_ID=? AND followers_ID=?");
        query.setParameter(1, user.getId());
        query.setParameter(2, usertoremove.getId());
        query.executeUpdate();
    }


    //TODO FIX REMOVE TWEET!!!
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

    @Override
    public  void initUsers(){
        Role user = new Role("user_role");
        Role admin = new Role("admin_role");


        User u1 = new User("Justin", "http://justinaarden.nl", "1-1-1111");
        this.create(u1);
        User u2 = new User("Hans", "no-website", "2-2-2222");
        this.create(u2);
        User u3 = new User("Sjaak", "no-website", "3-3-3333");
        this.create(u3);
        User u4 = new User("Tom", "no-website", "4-4-4444");
        this.create(u4);

        em.persist(user);
        em.persist(admin);

        u1.addGroup(user);
        u1.addGroup(admin);
        u2.addGroup(user);
        u3.addGroup(user);
        u4.addGroup(user);

        u1.addFollower(u2);
        u1.addFollower(u3);
        u2.addFollower(u3);

        u1.setPassword("password");
        u2.setPassword("password");
        u3.setPassword("password");
        u4.setPassword("password");

        this.edit(u1);
        this.edit(u2);
        this.edit(u3);
        this.edit(u4);

    }

}
