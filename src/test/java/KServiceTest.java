/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

import dao.UserDao;
import domain.Tweet;
import domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import service.KService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Justin on 29-2-2016.
*/

@RunWith(MockitoJUnitRunner.class)
public class KServiceTest {

    @InjectMocks
    KService kwetterService;

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("kwettertest_pu");
    EntityManager entityManager;
    @Mock
    UserDao userDao;

    User user1;
    User user2;


    Tweet tweet1;
    Tweet tweet2;

    @Before
    public void setup(){
        try {
            new CleanDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        user1 = new User("user1", "http://justinaarden.nl", "1-1-1111");
        entityManager.persist(user1);
        user2 = new User("user2", "http://justinaarden.nl", "2-2-2222");
        entityManager.persist(user2);

        tweet1 = new Tweet("tweet 1", new Date(), "PC");
        tweet2 = new Tweet("tweet 2", new Date(), "PC");
        entityManager.getTransaction().commit();
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
        kwetterService.findAll();
        Mockito.verify(userDao, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindOnId() throws Exception {
        kwetterService.find(1L);
        Mockito.verify(userDao, Mockito.times(1)).find(1L);
    }

    @Test
    public void testFindOnUsername() throws Exception {
        kwetterService.find("user1");
        Mockito.verify(userDao, Mockito.times(1)).find("user1");
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
    public void addTweetToUser() throws  Exception{
        entityManager.getTransaction().begin();
        Assert.assertEquals(user1.getTweets().size(),0);
        kwetterService.addTweet(user1,"Persittweet, doet @user2 het?", "UNITTEST");
        kwetterService.edit(user1);
        entityManager.getTransaction().commit();

        Assert.assertEquals(user1.getTweets().size(),1);

        entityManager.getTransaction().begin();
        kwetterService.addTweet(user2,"@user1 test", "UNITTEST");
        entityManager.getTransaction().commit();
        Assert.assertEquals(user2.getTweets().size(),1);
    }
}