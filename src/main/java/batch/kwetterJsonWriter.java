/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package batch;

/**
 * Created by Justin on 27-2-2016.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import domain.Tweet;
import domain.User;
import service.KService;

import javax.batch.api.chunk.ItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Dependent //http://docs.oracle.com/javaee/6/api/javax/enterprise/context/Dependent.html
@Named//("kwetterJsonWriter")
public class kwetterJsonWriter implements ItemWriter {

    @Inject
    KService kwetterService;

    @Override
    public void writeItems(List list) {
        for (Object resultArray : list) {
            Tweet tweet = (Tweet) ((Object[]) resultArray)[0];
            String name = (String) ((Object[]) resultArray)[1];
            if (tweet == null || name == null) {
                return;
            }
            User user = kwetterService.find(name);
            if (user == null) {
                user = new User(name, "http://inputofbatch/no-website", "Batchtesting - No bio"); //Create a user if user doesn't exit
                kwetterService.create(user);
            }
            user.addTweet(tweet);
            kwetterService.edit(user);
        }
    }

    /*
    * ===========================NiET NODIG=========================
    *
    * */

    @Override
    public void open(Serializable serializable) throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }























































}
