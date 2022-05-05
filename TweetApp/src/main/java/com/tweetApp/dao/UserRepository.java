package com.tweetApp.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tweetApp.model.UserModel;

public class UserRepository {
	Connection conn = JDBCConnection.getConnection();

	public boolean addUser(UserModel user) throws SQLException {
		if (conn != null) {
			try {
				String query = "Insert into usertable(firstname,lastname,username,gender,password,dob) values (?,?,?,?,?,?)";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, user.getFirstName());
				stmt.setString(2, user.getLastName());
				stmt.setString(3, user.getUsername());
				stmt.setString(4, user.getGender());
				stmt.setString(5, user.getPassword());
				stmt.setDate(6, user.getDob());
				stmt.execute();
				return true;
			} catch (Exception ex) {
				return false;
			}
		}
		return false;
	}

	public UserModel findbyId(String username) {
		UserModel user = null;
		if (conn != null) {
			try {

				String query = "Select * from usertable where username=?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, username);
				ResultSet quser = stmt.executeQuery();
				if (quser.next()) {
					user = new UserModel(quser.getString("firstname"), quser.getString("lastname"),
							quser.getString("username"), quser.getString("gender"), quser.getString("password"),
							quser.getDate("dob"));
				}
				return user;

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(e);
				return user;
			}
		}
		return user;
	}

	public List<UserModel> findAll() {
		List<UserModel> users = new ArrayList<UserModel>();
		if (conn != null) {
			try {
				Statement stmt = conn.createStatement();
				String query = "Select * from usertable";
				ResultSet quser = stmt.executeQuery(query);
				while (quser.next()) {
					UserModel user = new UserModel(quser.getString("firstname"), quser.getString("lastname"),
							quser.getString("username"), quser.getString("gender"), quser.getString("password"),
							quser.getDate("dob"));
					users.add(user);
				}
				return users;

			} catch (SQLException e) {
				e.printStackTrace();
				return users;
			}
		}
		return users;
	}

	public boolean updatePassword(UserModel user, String newPassword) {
		if (conn != null) {
			try {
				String query = "update usertable set password=? where username=?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, newPassword);
				stmt.setString(2, user.getUsername());
				System.out.println("Updated status");
				stmt.execute();
				
				return true;
			} catch (SQLException e) {
				System.out.println("unable to update status");
				return false;
			}

		}
		System.out.println("unable to update status");
		return false;
	}

}
