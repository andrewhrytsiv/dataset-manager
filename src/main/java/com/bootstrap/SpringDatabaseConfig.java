package com.bootstrap;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.dao.DatasetDAO;
import com.dao.DictionaryDAO;
import com.dao.UserDAO;
import com.dao.sql.DatasetDAOSql;
import com.dao.sql.DictionaryDAOSql;
import com.dao.sql.UserDAOSql;

@Configuration
@PropertySource("classpath:address.properties")
//"file:${app.home}/app.properties"
public class SpringDatabaseConfig {
	
	private static final String DRIVER_NAME = "db.driver";
	private static final String URL = "db.url";
	private static final String USERNAME = "db.username";
	private static final String PASSWORD = "db.password";
	
	@Resource
    private Environment env;
	
	@Bean
    public DataSource dataSource() {
		 DriverManagerDataSource dataSource = new DriverManagerDataSource();
         dataSource.setDriverClassName(env.getProperty(DRIVER_NAME));
         dataSource.setUrl(env.getProperty(URL));
         dataSource.setUsername(env.getProperty(USERNAME));
         dataSource.setPassword(env.getProperty(PASSWORD));
         return dataSource;
    }
	
	@Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }
	
	@Bean
	public UserDAO userDAO(){
		return new UserDAOSql();
	}
	
	@Bean
	public DatasetDAO datasetDAO(){
		return new DatasetDAOSql();
	}
	
	@Bean
	public DictionaryDAO dictionaryDAO(){
		return new DictionaryDAOSql();
	}
}
