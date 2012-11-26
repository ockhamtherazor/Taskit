/**
 * User.java
 * 2012-9-25 Qixuan Zhang Created.
 */

package com.taskit.shared;

/*
 * User class is the data structure used to store user information.
 * Username and password are required for each user, while Facebook credentials are not.
 */
public class User {
	
	private String userId;
	private String password;
	private String fbId;
	private String fbPassword;
	
	// Constructor for users without a Facebook account
	public User(String uid, String upass) {
		userId = uid;
		password = upass;
		fbId = null;
		fbPassword = null;
	}
	
	// Constructor for users with a facebook account
	public User(String uid, String upass, String fbid, String fbpass) {
		userId = uid;
		password = upass;
		fbId = fbid;
		fbPassword = fbpass;
	}
	
	// Getters and Setters
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserPassword() {
		return password;
	}
	
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	
	public String getFbId() {
		return fbId;
	}
	
	public void setFbPassword(String fbPassword) {
		this.fbPassword = fbPassword;
	}
	
	public String getFbPassword() {
		return fbPassword;
	}
	
	// Checker for user's Facebook ID
	public boolean hasFbId() {
		if (this.fbId != null) {
			return true;
		}
		else return false;
	}
	
}
