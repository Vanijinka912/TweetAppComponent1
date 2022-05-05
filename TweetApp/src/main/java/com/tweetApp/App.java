package com.tweetApp;

import java.sql.Date;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import com.tweetApp.exception.PasswordException;
import com.tweetApp.model.TweetModel;
import com.tweetApp.model.UserModel;
import com.tweetApp.service.TweetService;
import com.tweetApp.service.LoginService;
import com.tweetApp.serviceImpl.TweetServiceImpl;
import com.tweetApp.serviceImpl.LoginServiceImpl;

/**
 * Tweet App!
 *
 */
public class App {
	static Scanner sc = new Scanner(System.in);
	static boolean isLogged = false;
	private static LoginService loginService = new LoginServiceImpl();
	private static TweetService tweetService = new TweetServiceImpl();
	private static String username;

	public static void main(String[] args) {
		int option;
		
		while (true) {
			System.out.println("--------------Welcome to Tweet App------------");
			System.out.println("-------------------Menu-----------------------");
			System.out.println("----------------------------------------------");
			
			if (!isLogged) {
				System.out.println("1.Register\n2.Login\n3.Forgot Password");
				option = Integer.parseInt(sc.nextLine());
				switch (option) {
				case 1:
					System.out.println(register());
					break;
				case 2:
					isLogged = login();
					if (!isLogged) {
						username = null;
					}
					System.out.println(username);
					break;
				case 3:
					forgotPassword();
					break;
				default:
					System.out.println("Enter the correct option");
				}
			} else {
				System.out.println("1.Post a tweet\n2.View my tweets\n3.View all users\n4.View all tweets\n5.Reset Password\n6.Logout");
				option = Integer.parseInt(sc.nextLine());
				switch (option) {
				case 1:
					System.out.println(postATweet(username));
					break;
				case 2:
					List<String> tweets = viewMyTweets(username);
					if (tweets != null)
						tweets.forEach(System.out::println);
					else
						System.out.println("No tweets Found");
					break;
				case 3:
					List<String> users = viewAllUsers();
					if (users != null)
						users.forEach(System.out::println);
					else
						System.out.println("No users Found");
					break;
				case 4:
					List<String> alltweets = viewAllTweets();
					if (alltweets != null)
						alltweets.forEach(System.out::println);
					else
						System.out.println("No tweets Found");
					break;
				case 5:
					isLogged = resetPassword(username);
					break;
				case 6:
					isLogged = logOut();
					break;
				default:
					System.out.println("Enter the correct option");
				}
			}
		}
	}

	private static String register() {
		UserModel userDetails = new UserModel();
		System.out.println("In Register Method");
		System.out.println("Enter first Name");
		userDetails.setFirstName(sc.nextLine());
		System.out.println("Enter last Name");
		userDetails.setLastName(sc.nextLine());
		System.out.println("Enter gender Name");
		String gender = sc.nextLine();
		if (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("others")) {
			userDetails.setGender(gender);
		} else {
			System.out.println("Invalid Gender should be Male ,Female or Others");
			return "registration failed";
		}
		System.out.println("Enter Date of Birth in year-month-day format");
		String dob = sc.nextLine();
		try {
			userDetails.setDob(Date.valueOf(dob));
		} catch (Exception e) {
			System.out.println("Invalid Date Format");
			return "registration failed";
		}
		System.out.println("Enter User Name");
		String username = sc.nextLine();
		String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		if (Pattern.compile(regex).matcher(username).matches()) {
			userDetails.setUsername(username);
		} else {
			System.out.println("Username must follow proper email standards");
			return "registration failed";
		}
		System.out.println("Enter Password");
		String pass = sc.nextLine();
		if (pass.length() < 5 || pass.length() > 10) {
			System.out.println("Password must be more or equal to 8 characters and less than 10 characters");
			return "registration failed";
		}
		userDetails.setPassword(pass);
		return loginService.register(userDetails);
	}

	public static boolean login() {
		System.out.println("In Login Method");
		System.out.println("Enter User Name");
		String uname = sc.nextLine();
		System.out.println("Enter Password");
		String password = sc.nextLine();
		if (uname == null || password == null || uname.trim().isEmpty() || password.trim().isEmpty()) {
			System.out.println("Login unsuccessful --> User name or password is empty");
			return false;
		} else {
			username = uname;
			return loginService.login(username, password);
		}
	}

	public static String postATweet(String username) {
		TweetModel tweet = new TweetModel();
		tweet.setUsername(username);
		System.out.println("Enter your tweet to post");
		tweet.setTweet(sc.nextLine());
		return tweetService.postATweet(tweet);
	}

	public static List<String> viewAllUsers() {
		try {
			return loginService.viewAllUsers();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public static List<String> viewMyTweets(String username) {
		try {
			return tweetService.viewMyTweets(username);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public static List<String> viewAllTweets() {
		try {
			return tweetService.viewAllTweets();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public static boolean resetPassword(String username) {
		try {
			System.out.println("Enter your old password");
			String oldPassword = sc.nextLine();
			System.out.println("Enter your new password");
			String newPassword = sc.nextLine();
			System.out.println("Re-Enter your new password");
			String newCheckPassword = sc.nextLine();
			if (newPassword.length() < 5 || newPassword.length() > 10) {
				System.out.println("Password should be between 5 to 10 characters");
				return true;
			}
			if (!newPassword.equals(newCheckPassword)) {
				throw new PasswordException("Re-entered password mismatch");
			}
			if (newPassword.equals(oldPassword)) {
				throw new PasswordException("old password and new password should not be same");
			}
			if (loginService.resetPassword(username, oldPassword, newPassword)) {
				System.out.println("Reset successfully");
				return false;
			}
			System.out.println("Reset Failed");
			return true;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return true;
		}
	}

	public static void forgotPassword() {
		try {
			System.out.println("Enter your username");
			String username = sc.nextLine();
			System.out.println("Enter your new password");
			String newPassword = sc.nextLine();
			System.out.println("Re-Enter your new password");
			String newCheckPassword = sc.nextLine();
			if (newPassword.length() < 5 || newPassword.length() > 10) {
				System.out.println("Password should be between 5 to 10 characters");
			}
			if (!newPassword.equals(newCheckPassword)) {
				throw new PasswordException("Re-entered password mismatch");
			}
			if (loginService.forgotPassword(username, newPassword)) {
				System.out.println("Reset successfully");
			} else
				System.out.println("Reset Failed");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static boolean logOut() {
		if (loginService.logout(username)) {
			System.out.println("Logged out successfully");
			return false;
		}
		System.out.println("Logout Failed");
		return true;
	}
}
