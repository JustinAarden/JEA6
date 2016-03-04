/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package controller;

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
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;



@Named(value="UserBean")
@RequestScoped
public class UserBean {

    private String result;
    private User user;
    private String userName;



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

    public void CheckValidUser(){
        User user = kwetterService.find(userName);

        if(user != null){

            try {
                goToMainPageByUserID(user.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //result = "This is a existing user";
        }
        else{
            result = "This user doesn't exist";
        }




    }

    public void goToMainPageByUserID(Long id) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("main.xhtml?id=" + id);

    }





}
