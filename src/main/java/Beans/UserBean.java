/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package Beans;

import domain.Role;
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
    private Long refId = 0L;
    private Long id;


    public Long getId() {
        return id;
    }
    public ArrayList<User> getFollowing() {
        return following;
    }
    public ArrayList<User> getFollowers() {
        return followers;
    }
    public User getUser() {
        return user;
    }

    public void setFollowers() {
            followers.addAll(user.getFollowers());

    }
    public void setFollowing() {
            following.addAll(kwetterService.findFollowing(user.getId()));

    }
    public void addFollower(){
        if(!refId.equals(0L)){
            user.addFollower(kwetterService.find(refId));
        }else{
            try {
                gotToLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    private boolean isNullOrBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }

    public void gotToLogin() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");

    }


    public void goToMainPageByUserID(Long id) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("user.xhtml?id=" + id);

    }

    public void init(){
        if(!isNullOrBlank(request.getParameter("id"))){
            id = Long.parseLong(request.getParameter("id"));
            user = kwetterService.find(id);
        }else{
            try {
                gotToLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!isNullOrBlank(request.getParameter("ref"))){
            refId = Long.parseLong(request.getParameter("ref"));
        }


        if(!kwetterService.findFollowing(user.getId()).isEmpty()){
            setFollowing();

        }
        if(!user.getFollowers().isEmpty()){
            setFollowers();
        }
    }

    public boolean hasRole(String role) {
        return user.getRoles().contains(role);
    }

    public String getRoles(){
        String test = "";
        for (Role role: user.getRoles()
             ) {
            test += role.getGroupId();

        }
        return  test;

    }



}
