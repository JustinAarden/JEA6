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

import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
@Dependent
@Named("kwetterJsonWriter")
public class kwetterJsonWriter extends AbstractItemWriter {

    @Inject
    KService kwetterService;

    @Override
    public void writeItems(List list) {
        //System.out.println("writeItems: " + list);
        for (Object resultArray : list) {
            //System.out.println(Arrays.deepToString((Object[])resultArray));
            Tweet tweet = (Tweet) ((Object[]) resultArray)[0];
            String name = (String) ((Object[]) resultArray)[1];
            if (tweet == null || name == null) {
                return;
            }
            User user = kwetterService.find(name);
            if (user == null) {
                user = new User(name, "http://localhost:8080", "No bio");
                kwetterService.create(user);
            }
            user.addTweet(tweet);
            kwetterService.edit(user);
        }
    }
}
