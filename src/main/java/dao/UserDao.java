package dao;

import domain.User;

import java.util.List;

public interface UserDao {


    int count();

    void create(User user);

    void edit(User user);

    List<User> findAll();

    User find(Long id);

    User find(String name);

    void remove(User user);

    Long nextTweetID();

    void addFollower(User userToFollow, User follower);

}