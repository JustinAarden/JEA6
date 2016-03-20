/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package dao;

import domain.Tweet;

import java.util.List;

/**
 * Created by Justin on 16-3-2016.
 */
public interface TweetDao {

    List<Tweet> getMentionedTweets(Long id);
}
