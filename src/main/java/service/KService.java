

/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package service;

import dao.UserDao;
import domain.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;


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

    /**
     *This method creates a user and persist it in a database
     *
     * @param user
     */
    public void create(User user) {
        userdao.create(user);
    }

    /**
     *  and merge it in a database
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

    public List<User>  findFollower(Object id) {
        return userdao.findFollower((Long)id);
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