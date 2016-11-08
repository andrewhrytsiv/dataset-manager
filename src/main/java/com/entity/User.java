package com.entity;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class User {
	
	private Integer id;
	private String email;
	private String userName;
	private String password;
	private int role;
	
	public User(){}
	
	public User(String email,String userName, String password){
		this.email = email;
		this.userName = userName;
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRole() {
		return role;
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "User [ email=" + email + ", userName=" + userName + "]";
	}
	
	public static String hash(String password){
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
