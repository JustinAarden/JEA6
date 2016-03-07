/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

import dao.UserDao;
import domain.Tweet;
import domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import service.KService;

import java.util.Date;

/**
 * Created by Justin on 29-2-2016.
*/

@RunWith(MockitoJUnitRunner.class)
public class KServiceTest {

    @InjectMocks
    KService kwetterService;

    @Mock
    UserDao userDao;

    User user1;
    User user2;



    Tweet tweet1;
    Tweet tweet2;

    @Before
    public void setup(){
        user1 = new User("user1", "http://justinaarden.nl", "1-1-1111");
        user2 = new User("user2", "http://justinaarden.nl", "2-2-2222");

        tweet1 = new Tweet("tweet 1", new Date(), "PC");
        tweet2 = new Tweet("tweet 2", new Date(), "PC");
    }

    @Test
    public void testCreate() throws Exception {

        kwetterService.create(user1);
        Mockito.verify(userDao, Mockito.times(1)).create(user1);
    }

    @Test
    public void testEdit() throws Exception {
        kwetterService.create(user1);
        Mockito.verify(userDao, Mockito.never()).edit(user1);
        user1.addTweet(tweet1);
        kwetterService.edit(user1);
        Mockito.verify(userDao, Mockito.times(1)).edit(user1);
    }


    @Test
    public void testFindAll() throws Exception {




    }

    @Test
    public void testFind() throws Exception { // find on id
        kwetterService.find(1L);
        // h
        Mockito.verify(userDao, Mockito.times(1)).find(1L);
    }

    @Test
    public void testFind1() throws Exception { //find on username

    }

    @Test
    public void testCount() throws Exception {

        kwetterService.create(user1);
        Mockito.verify(userDao, Mockito.never()).count();
        user1.addTweet(tweet1);
        kwetterService.edit(user1);
        Mockito.verify(userDao, Mockito.never()).count();
    }

    @Test
    public void testNextTweetID() throws Exception {

    }
}