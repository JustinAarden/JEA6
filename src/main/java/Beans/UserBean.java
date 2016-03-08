/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package Beans;

import domain.User;
import service.KService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Justin on 4-3-2016.
 */
@ManagedBean(name = "UserBean")
@ViewScoped
public class UserBean {
    @Inject
    KService kwetterService;

    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
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



    public User getUser() {
        return user;
    }




 void gotToErrorPage() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
 return;}
    private boolean isNullOrBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }
    public void init(){

        if(!isNullOrBlank(request.getParameter("id"))){
            Long id = Long.parseLong(request.getParameter("id"));
            user = kwetterService.find(id);
        }else{
        }



        if(!user.getFollowing().isEmpty()){
            setFollowing();

        }
        if(!user.getFollowers().isEmpty()){
            setFollowers();
        }
    }




}
