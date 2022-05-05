package com.tweetApp.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;
import com.tweetApp.dao.TweetRepository;
import com.tweetApp.model.TweetModel;
import com.tweetApp.service.TweetService;

public class TweetServiceImpl implements TweetService {

	TweetRepository tweetRepository = new TweetRepository();

	@Override
	public String postATweet(TweetModel tweet) {
		try {
			tweetRepository.postTweet(tweet);
			return "Tweet added Successfully";
		} catch (Exception ex) {
			System.out.println("Failed to add tweet");
			return ex.getMessage();
		}

	}

	@Override
	public List<String> viewMyTweets(String username) {
		System.out.println("Getting your tweets");

		return tweetRepository.findAll().stream().filter(tweet -> tweet.getUsername().equals(username))
				.map(TweetModel::getTweet).collect(Collectors.toList());
	}
	
	@Override
	public List<String> viewAllTweets() {
		System.out.println("Getting all tweets");
		return tweetRepository.findAll().stream().map(tweet -> tweet.getTweet()).collect(Collectors.toList());

	}

}
