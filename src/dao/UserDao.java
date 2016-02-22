package dao;

import domain.User;
import java.util.List;

public interface UserDao {

    int count();

    void create(User user);

    void edit(User user);

    List<User> findAll();

    User find(Long id);

    void remove(User user);

    Long nextTweetID();
}