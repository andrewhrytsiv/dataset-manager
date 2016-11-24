package com.entity;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

public class MetadataKeyValue {
	
	private UUID uuid;
	private String table;
	private Map<String,String> keyValue = Maps.newHashMap();
	
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public Map<String, String> getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(Map<String, String> keyValue) {
		this.keyValue = keyValue;
	}
}
