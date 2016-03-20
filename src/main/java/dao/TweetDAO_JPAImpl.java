/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package dao;

import domain.Tweet;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

/**
 * Created by Justin on 16-3-2016.
 */

@Local(TweetDao.class)
//@ApplicationScoped
@Stateless
public class TweetDAO_JPAImpl implements TweetDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Tweet> getMentionedTweets(Long id) {
        if (id == null) return Collections.emptyList();
        return em.createQuery("SELECT tweet FROM Tweet tweet Join tweet.mentioned mention WHERE mention.id=:userid").setParameter("userid", id).getResultList();
    }


}