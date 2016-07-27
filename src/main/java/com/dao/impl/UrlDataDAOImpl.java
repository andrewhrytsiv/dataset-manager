package com.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.dao.UrlDataDAO;
import com.entity.UrlData;

public class UrlDataDAOImpl implements UrlDataDAO{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertUrlData;
	
	@Autowired 
    public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertUrlData = new SimpleJdbcInsert(dataSource).withTableName("url_data").usingGeneratedKeyColumns("id");
    } 
	@Override
	public void insert(UrlData data, byte[] bytes) {
		LobHandler lobHandler = new DefaultLobHandler(); 
		jdbcTemplate.update("INSERT INTO url_data (url, file, file_type) VALUES (?, ?, ?)",
				new Object[] { data.getUrl(),new SqlLobValue(bytes), data.getType() },
				new int[] { Types.VARCHAR, Types.BLOB, Types.VARCHAR });
	
	}
	@Override
	public void insert(UrlData data) {
		// TODO Auto-generated method stub
		
	}
	

}
