package com.tweetApp.service;

import java.util.List;
import com.tweetApp.model.UserModel;

public interface LoginService {
    String register(UserModel userDetails);
    boolean login(String username,String password);
    boolean logout(String username);
    List<String> viewAllUsers();
    boolean resetPassword(String username,String old,String newPassword);
	boolean forgotPassword(String username, String newPassword);
}
