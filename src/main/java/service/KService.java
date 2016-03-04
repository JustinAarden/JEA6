

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
    } //TODO not implemented correctly

    /**
     *
     * @return
     */
    public List<User> findAll() {
        return userdao.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public User find(Object id) {
        return userdao.find((Long)id);
    }

    /**
     *
     * @param name
     * @return
     */
    public User find(String name){
        return userdao.find(name);
    } //find on name

    /**
     *
     * @return
     */
    public int count() {
        return userdao.count();
    }

    /**
     *
     * @return
     */
    public Long nextTweetID(){
        return userdao.nextTweetID();
    }
}