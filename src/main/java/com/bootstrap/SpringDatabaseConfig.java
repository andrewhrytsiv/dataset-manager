package com.bootstrap;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.dao.UrlDataDAO;
import com.dao.UserDAO;
import com.dao.impl.UrlDataDAOImpl;
import com.dao.impl.UserDAOImpl;

@Configuration
public class SpringDatabaseConfig {
	
	private static final String DRIVER_NAME = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql://localhost/test_db";
	private static final String USERNAME = "andrew";
	private static final String PASSWORD = "andrew";
	
	@Bean
    public DataSource dataSource() {
		 DriverManagerDataSource dataSource = new DriverManagerDataSource();
         dataSource.setDriverClassName(DRIVER_NAME);
         dataSource.setUrl(URL);
         dataSource.setUsername(USERNAME);
         dataSource.setPassword(PASSWORD);
         return dataSource;
    }
	
	@Bean
	public UserDAO userDAO(){
		UserDAO userDAO = new UserDAOImpl();
		return userDAO;
	}
	
	@Bean
	public UrlDataDAO usrlDataDAO(){
		UrlDataDAO urlDataDAO = new UrlDataDAOImpl();
		return urlDataDAO;
	}
}
