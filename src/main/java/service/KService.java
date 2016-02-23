

package service;

import dao.UserDAOCollectionImpl;
import dao.UserDao;
import domain.User;

import javax.faces.bean.ApplicationScoped;
import java.util.List;


@ApplicationScoped


public class KService {

    //@Inject
    private UserDao userdao;

    private final static KService instance = new KService();
    private KService(){userdao = new UserDAOCollectionImpl();}

    public static KService instance() {
        return instance;
    }

    public void create(User user) {
        userdao.create(user);
    }

    public void edit(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void remove(User user) {
        userdao.remove(user);
    }

    public List<User> findAll() {
        return userdao.findAll();
    }

    public User find(Object id) {
        return userdao.find((Long)id);
    }

    public int count() {
        return userdao.count();
    }

    public Long nextTweetID(){
        return userdao.nextTweetID();
    }

}