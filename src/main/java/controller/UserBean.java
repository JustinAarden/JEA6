/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package controller;

import domain.User;
import service.KService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by Justin on 4-3-2016.
 */
@ManagedBean(name = "UserBean")
@ViewScoped
public class UserBean {
    @Inject
    KService kwetterService;

    private User user;




    private ArrayList<User> followers = new ArrayList<>();

    private ArrayList<User> following = new ArrayList<>();

    public ArrayList<User> getFollowing() {
        return following;
    }
    public ArrayList<User> getFollowers() {
        return followers;
    }
    public void setFollowers() {
        for (Long forId : user.getFollowers()) {
            followers.add(kwetterService.find(forId));
        }

    }
    public void setFollowing() {
        for (Long forId : user.getFollowing())
        {
            following.add(kwetterService.find(forId));

        }
    }


    public void init(Long id){
        user = new User();
        user = kwetterService.find(id);
        if(!user.getFollowing().isEmpty()){
            setFollowing();

        }
        if(!user.getFollowers().isEmpty()){
            setFollowers();
        }
    }


}
