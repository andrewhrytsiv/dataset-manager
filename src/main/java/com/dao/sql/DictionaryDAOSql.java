package com.dao.sql;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dao.DictionaryDAO;
import com.dao.sql.mapper.DictionaryRowMapper;
import com.entity.Dictionary;
import com.google.common.collect.Lists;

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
	public Dictionary findDictionary(String key){
		String sql = "SELECT * FROM dictionary WHERE key = ? ";
		Dictionary dictionary = (Dictionary)jdbcTemplate.queryForObject(sql, new Object[] {key}, new DictionaryRowMapper());
		return dictionary;
	}
	
	@Override
	public List<Dictionary> findDictionaries(String type){
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
		
	}
	
	@Override
	public void insert(List<Dictionary> dictionaryList){
		
	}
}
