/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package Beans;

/**
 * Created by Justin on 2-3-2016.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import domain.User;
import service.KService;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Named(value="LoginBean")
@RequestScoped
public class LoginBean {

    private String result;
    private User user;
    private String userName;

    @Inject
    HttpServletRequest request;
    @Inject
    HttpSession session;

    private  String password;
    private String allUsers = "";


    Long id;
    @Inject
    KService kwetterService;
    //private String subContent = "tweets";

    public User getUser() {
        return user;
    }

    public String getResult(){
        return result;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    private NavigationBean navigationBean =new NavigationBean();





    public String findUserOnID(Long id){
        User user = kwetterService.find(id);
        userName = user.getName();
                return userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null;
    }

    public String login() {
        System.out.println("login()");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        session = request.getSession();
        try {
            request.login(this.userName, this.password);
            user = kwetterService.find(userName);
            session.setAttribute("username", user.getId());
        } catch (ServletException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Login failed."));

            return "error";
        }
        Logger.getGlobal().log(Level.SEVERE,"USER: " + user.getName() + getPassword());
        return "/faces/private/index.xhtml";
    }

    public void CheckValidUser(){
        User user = kwetterService.find(userName);

        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.login(user.getName(), password);
            if(request.isUserInRole("user_role")){
                result = "you are a user";
                goToMainPageByUserID(user.getId());
            }else{
                result = "you are a nothing";
                goToMainPageByUserID(user.getId());
            }


        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

/*        if(user != null){

            try {
               // goToMainPageByUserID(user.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //result = "This is a existing user";
        }
        else{
            result = "This user doesn't exist";
        }*/

    }

    public void goToMainPageByUserID(Long id) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("user.xhtml?id=" + id);

    }
    /**
     *
     * @return
     */
    public String getAllUsers(){
        List<User> userlist;
        userlist = kwetterService.findAll();

        for (User user: userlist
             ) {
            allUsers +=  user.getName() + " : " + user.getPassword() + "\n";

        }
        return allUsers;
    }

    public void signOut() {
        try {
            request.logout();
            session.invalidate();
            System.out.println("Signout invoked");
/*            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation
                    (FacesContext.getCurrentInstance(), null, "/faces/index.xhtml");*/
            navigationBean.goToIndex();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Signout -" +  ex.getMessage());
        }
    }



}
