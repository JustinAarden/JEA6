

package service;

import dao.UserDao;
import domain.User;

import javax.ejb.Stateless;
import java.util.List;



@Stateless
public class KService {


    private UserDao userDAO;

    public KService() {
    }

    public void create(User user) {
        userDAO.create(user);
    }

    public void edit(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void remove(User user) {
        userDAO.remove(user);
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User find(Object id) {
        return userDAO.find((Long)id);
    }

    public int count() {
        return userDAO.count();
    }

    public Long nextTweetID(){
        return userDAO.nextTweetID();
    }

}