package com.dao;

import com.entity.User;

public interface UserDAO {
	
	public void insert(User user);
	
	public User find(String email);
	
	public User find(Integer id);
	
}
