package com.tweetApp.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;
import com.tweetApp.dao.UserRepository;
import com.tweetApp.exception.PasswordException;
import com.tweetApp.exception.UserExistsException;
import com.tweetApp.exception.UserNotFoundException;
import com.tweetApp.model.UserModel;
import com.tweetApp.service.LoginService;

public class LoginServiceImpl implements LoginService {

	UserRepository userRepository = new UserRepository();

	@Override
	public String register(UserModel userDetails) {
		System.out.println("In service for adding user ");
		try {
			UserModel user = userRepository.findbyId(userDetails.getUsername());
			if (user == null) {
				userRepository.addUser(userDetails);
				return "Registration successful, Login in...";
			}
			throw new UserExistsException("User already registered, Login Now");
		} catch (Exception ex) {
			return ex.getMessage();
		}

	}

	@Override
	public boolean login(String username, String password) {
		System.out.println("In service for login user ");
		try {
			UserModel user = userRepository.findbyId(username);
			if (user == null) {
				throw new UserNotFoundException("Username Not Found, Resister");
			}
			if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
				System.out.println("Login Successful");
				return true;
			}
			System.out.println("Password Incorrect ,Retry again");
			return false;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}

	}

	@Override
	public List<String> viewAllUsers() {
		System.out.println("In user service to get all the users");
		return userRepository.findAll().stream().map(o -> o.getUsername()).collect(Collectors.toList());
	}

	@Override
	public boolean resetPassword(String username, String oldPassword, String newPassword) {
		System.out.println("In service for reset user password");
		try {
			UserModel user = userRepository.findbyId(username);
			if (user == null) {
				throw new UserNotFoundException("Username Not Found, Resister");
			}
			if (user.getPassword().equals(oldPassword)) {
				user.setPassword(newPassword);
				userRepository.updatePassword(user, newPassword);
				System.out.println("Password reset Successful , Login again");
				return true;
			}
			throw new PasswordException("Old password and new Password Not Matched");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}

	}

	@Override
	public boolean logout(String username) {
		System.out.println("In service for logout user ");
		try {
			UserModel user = userRepository.findbyId(username);
			if (user == null) {
				throw new UserNotFoundException("Username Not Found, Resister");
			}
			System.out.println("Logout Successful");
			return true;

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean forgotPassword(String username, String newPassword) {

		try {
			UserModel user = userRepository.findbyId(username);
			if (user == null) {
				throw new UserNotFoundException("Username Not Found, Resister");
			}

			boolean result = userRepository.updatePassword(user, newPassword);
			if (result)
				System.out.println("Password Reset Successful , Login again");
			else
				System.out.println("Password reset unsuccessful");
			return true;
		} catch (UserNotFoundException e1) {
			System.out.println(e1.getMessage());
			return false;
		}
	}
}
