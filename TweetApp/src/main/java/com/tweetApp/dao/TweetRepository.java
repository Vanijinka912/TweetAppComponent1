package com.tweetApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tweetApp.model.TweetModel;

public class TweetRepository {
	Connection conn = JDBCConnection.getConnection();

	public boolean postTweet(TweetModel tweet) throws SQLException {
		if (conn != null) {

			String query = "insert into tweettable(username,tweet) values (?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, tweet.getUsername());
			stmt.setString(2, tweet.getTweet());
			return stmt.execute();

		}
		return false;
	}

	public List<TweetModel> findAll() {
		List<TweetModel> tweets = new ArrayList<TweetModel>();
		if (conn != null) {
			try {
				Statement stmt = conn.createStatement();
				String query = "select * from tweettable";
				ResultSet result = stmt.executeQuery(query);
				while (result.next()) {
					TweetModel tweet = new TweetModel(result.getString("tweet"), result.getString("username"));
					tweets.add(tweet);
				}
				return tweets;
			} catch (Exception ex) {
				return tweets;
			}
		}
		return tweets;
	}

}
