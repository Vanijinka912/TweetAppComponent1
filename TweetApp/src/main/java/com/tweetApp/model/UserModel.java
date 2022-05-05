package com.tweetApp.model;

import java.sql.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
	
	private String firstName;
	private String lastName;
	private String username;
	private String gender;
	private String password;
	private Date dob;
}
