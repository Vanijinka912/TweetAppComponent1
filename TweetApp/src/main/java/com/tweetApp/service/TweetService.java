package com.tweetApp.service;


import java.util.List;
import com.tweetApp.model.TweetModel;

public interface TweetService {
    String postATweet(TweetModel tweet);
    List<String> viewMyTweets(String username);
    List<String> viewAllTweets();
}
