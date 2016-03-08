/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import domain.Tweet;
import domain.User;
import service.KService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/KwetterGo/topic"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"), //Durable not durable (Case sensitive)
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/KwetterGo/topic")
})
public class NewMessageBean implements MessageListener {

    private static final Logger LOG = Logger.getLogger(NewMessageBean.class.getName());

    @Inject
    KService kwetterService;

    public NewMessageBean() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageString = message.getBody(String.class);
            System.out.println("Got a message:");
            System.out.println(messageString);

            String messageText = messageString.split(" --- ")[0];
            String messageUsername = messageString.split(" --- ")[1];

            User user = kwetterService.find(messageUsername);
            if (user == null) {
                System.out.println("Can't find user with name " + messageUsername);
                return;
            }
            user.addTweet(new Tweet(messageText, new Date(), "Message Bean"));
            kwetterService.edit(user);

              } catch (JMSException ex) {
            LOG.log(Level.SEVERE, "Error in message: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

}
