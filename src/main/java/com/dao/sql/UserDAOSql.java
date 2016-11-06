package com.dao.sql;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.dao.UserDAO;
import com.entity.User;

@Repository 
public class UserDAOSql implements UserDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertUser;
	
	@Autowired 
    public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertUser = new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("user_id");
    } 
	
	@Override
	public void insert(User user) {
		 Map<String, Object> parameters = new HashMap<String, Object>(3);
	     parameters.put("user_name", user.getUserName());
	     parameters.put("email", user.getEmail());
	     parameters.put("password", user.getPassword());
	     Number newId = insertUser.executeAndReturnKey(parameters);
	     user.setId(newId.intValue());
	}

	@Override
	public User find(String email) {
		String sql = "SELECT * FROM users WHERE email = ? ";
		User user = (User)jdbcTemplate.queryForObject(sql, new Object[] {email}, new UserRowMapper());
		return user;
	} 

}
