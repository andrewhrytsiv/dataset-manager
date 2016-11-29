package com.dao.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.entity.Dictionary;

public class DictionaryRowMapper implements RowMapper<Dictionary>{

	@Override
	public Dictionary mapRow(ResultSet rs, int rowNum) throws SQLException {
		Dictionary dictionary = new Dictionary();
		dictionary.setKey(rs.getString("key"));
		dictionary.setType(rs.getString("type"));
		dictionary.setDictionaryJson(rs.getString("dictionary_json"));
		return dictionary;
	}

}
