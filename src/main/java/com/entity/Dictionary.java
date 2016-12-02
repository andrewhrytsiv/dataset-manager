package com.entity;

import java.sql.SQLException;

import org.postgresql.util.PGobject;

public class Dictionary {

	private String key;
	private String type;
	private String dictionaryJson;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}
	
	public String getNullTypeAsString(){
		return (type == null)? "null" : type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDictionaryJson() {
		return dictionaryJson;
	}

	public void setDictionaryJson(String dictionaryJson) {
		this.dictionaryJson = dictionaryJson;
	}

	public Object getPGJson() throws SQLException {
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(dictionaryJson);
		return jsonObject;
	}
}
