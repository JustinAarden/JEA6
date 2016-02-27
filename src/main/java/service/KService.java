

package service;

import dao.UserDao;
import domain.User;

import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;


@ApplicationScoped
@Stateless

public class KService {

    @Inject
    private UserDao userdao;


    public KService() {
    }

    public void create(User user) {
        userdao.create(user);
    }

    public void edit(User user) {
        userdao.edit(user);
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

    public User find(String name){
        return userdao.find(name);
    }

    public int count() {
        return userdao.count();
    }

    public Long nextTweetID(){
        return userdao.nextTweetID();
    }
}