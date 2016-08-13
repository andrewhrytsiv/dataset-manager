package com.dao.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.entity.User;

public class UserRowMapper implements RowMapper<Object>{

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("user_id"));
		user.setRole(rs.getInt("role_id"));
		user.setUserName(rs.getString("user_name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("user_name"));
		return user;
	}

}
