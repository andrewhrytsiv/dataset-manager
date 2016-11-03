package com.datasets.query;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

public class RowData implements Comparable<RowData>{

	private Map<String, String> keyValue = Maps.newLinkedHashMap();
	
	public RowData add(String key, String value){
		keyValue.put(key, value);
		return this;
	}
	
	@Override
	public String toString(){
		Gson gson = new Gson(); 
		return gson.toJson(keyValue);
	}
	
	@Override
	public int compareTo(RowData o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
