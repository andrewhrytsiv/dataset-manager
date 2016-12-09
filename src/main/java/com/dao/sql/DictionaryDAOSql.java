package com.dao.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dao.DictionaryDAO;
import com.dao.sql.mapper.DictionaryRowMapper;
import com.entity.Dictionary;
import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.util.Utility;

public class DictionaryDAOSql implements DictionaryDAO{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryDAOSql.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
    public void setDataSource(DataSource source) {
		this.dataSource = source;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@Override
	public Table<String,String,Boolean> exist(List<Dictionary> dictionaryList) {
		Table<String,String,Boolean> existTable = HashBasedTable.create();
		List<String> keyIds = dictionaryList.stream().map( d -> Utility.wrapSingleQuotese(d.getKey())).collect(Collectors.toList());
		//Not secure sql. To-do in future
		String sql = "SELECT key, type FROM dictionary WHERE key IN (" + Joiner.on(",").join(keyIds)+")";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : rows) {
			String key = (String)row.get("key");
			String type = (String)row.get("type");
			if(type == null){
				type = "null";
			}
			existTable.put(key, type, Boolean.TRUE);
		}
		return existTable;
	}
	
	@Override
	public Dictionary find(String key, String typ) {
		String sql = "SELECT * FROM dictionary WHERE key = ? AND  type = ?";
		Dictionary dictionary = (Dictionary)jdbcTemplate.queryForObject(sql, new Object[] {key}, new DictionaryRowMapper());
		return dictionary;
	}
	
	@Override
	public List<Dictionary> findAll() {
		String sql = "SELECT * FROM dictionary";
		List<Dictionary> dictionaryList = Lists.newArrayList();
		List<Map<String, Object>> rows  = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : rows) {
			try{
				Dictionary dictionary = new Dictionary();
				dictionary.setKey((String)row.get("key"));
				dictionary.setType((String)row.get("type"));
				PGobject postgreJson = (PGobject)row.get("dictionary_json");
				dictionary.setDictionaryJson(postgreJson.getValue());
				dictionaryList.add(dictionary);
			}catch(Exception ex){
				LOGGER.error("Retrive dictionary error:", ex);
			}
		}
		return dictionaryList;
	}
	
	@Override
	public List<Dictionary> findDictionariesByKey(String key){
		String sql = "SELECT * FROM dictionary WHERE key = ? ";
		List<Dictionary> dictionaryList = Lists.newArrayList();
		List<Map<String, Object>> rows  = jdbcTemplate.queryForList(sql, new Object[] {key}, new DictionaryRowMapper());
		for (Map<String, Object> row : rows) {
			Dictionary dictionary = new Dictionary();
			dictionary.setKey((String)row.get("key"));
			dictionary.setType((String)row.get("type"));
			dictionary.setDictionaryJson((String)row.get("dictionary_json"));
			dictionaryList.add(dictionary);
		}
		return dictionaryList;
	}
	
	@Override
	public List<Dictionary> findDictionariesByType(String type){
		String sql = "SELECT * FROM dictionary WHERE type = ? ";
		List<Dictionary> dictionaryList = Lists.newArrayList();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[] {type});
		for (Map<String, Object> row : rows) {
			Dictionary dictionary = new Dictionary();
			dictionary.setKey((String)row.get("key"));
			dictionary.setType((String)row.get("type"));
			dictionary.setDictionaryJson((String)row.get("dictionary_json"));
			dictionaryList.add(dictionary);
		}
		return dictionaryList;
	}
	
	@Override
	public void insert(Dictionary dictionary){
		String sql = "INSERT INTO dictionary (key, type, dictionary_json) VALUES (?, ?, ?)";
		try {
			jdbcTemplate.update(sql, new Object[] {dictionary.getKey(), dictionary.getType(), dictionary.getPGJson()});
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} 
	}
	
	@Override
	public void insert(List<Dictionary> dictionaryList){
		String sql = "INSERT INTO dictionary (key, type, dictionary_json) VALUES (?, ?, ?)";
		List<Object[]> parameters = dictionaryList.stream()
				.map(dictionary -> {
					try {
						return new Object[] {dictionary.getKey(), dictionary.getType(), dictionary.getPGJson()};
					} catch (SQLException e) {
						LOGGER.error("Not valid disctionary json");
						return null;
					}
				})
				.collect(Collectors.toList());
		jdbcTemplate.batchUpdate(sql, parameters);
		LOGGER.info("Dictionaries inserted successfully");
	}
	
	@Override
	public void update(Dictionary dictionary){
		String sql = "UPDATE dictionary SET dictionary_json = ? WHERE key = ? AND  type = ?";
		try {
			jdbcTemplate.update(sql, new Object[] {dictionary.getPGJson(), dictionary.getKey(), dictionary.getType()});
		} catch (Exception e) {
			LOGGER.error("Can't update dictionary" + e.getMessage());
		} 
	}

	@Override
	public void update(List<Dictionary> dictionaryList) {
		String sql = "UPDATE dictionary SET dictionary_json = ? WHERE key = ? AND  type = ?";
		List<Object[]> parameters = dictionaryList.stream()
				.map(dictionary -> {
					try {
						return new Object[] {dictionary.getPGJson(), dictionary.getKey(), dictionary.getType() };
					} catch (SQLException e) {
						LOGGER.error("Not valid disctionary json");
						return null;
					}
				})
				.collect(Collectors.toList());
		jdbcTemplate.batchUpdate(sql, parameters);
		LOGGER.info("Dictionaries updated successfully");
	}
}
