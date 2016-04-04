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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Justin on 4-3-2016.
 */
@ManagedBean(name = "UserBean")
@ViewScoped
public class UserBean {
    @Inject
    KService kwetterService;
    @Inject
    HttpServletRequest request;
    @Inject
    HttpSession session;

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

    public String getUserPrincipalName() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Principal principal = fc.getExternalContext().getUserPrincipal();
        if(principal == null) {
            return null;
        }
        return principal.getName();
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
        }

    }




    private boolean isNullOrBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }



    public void goToMainPageByUserID(Long id) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("user.xhtml?id=" + id);

    }

    public void init(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        session = request.getSession();
        user = kwetterService.find(getUserPrincipalName());
        Logger.getGlobal().log(Level.SEVERE,"userbean USER: " + user.getName() + user.getPassword());
/*        if(!isNullOrBlank(request.getParameter("id"))){
            id = Long.parseLong(request.getParameter("id"));
            user = kwetterService.find(id);
        }else{
            try {
                gotToLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
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
