package com.datasets.query;

import java.util.Map;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

public class RowData implements Comparable<RowData>{

	private Map<String, String> keyValue = Maps.newLinkedHashMap();
	
	public RowData add(String key, String value){
		keyValue.put(key, value);
		return this;
	}
	
	public Map<String, String> getProperties(){
		return keyValue;
	}
	
	@Override
	public int compareTo(RowData another) {
		ComparisonChain chain = ComparisonChain.start();
		keyValue.entrySet().forEach( entry -> {
			String key = entry.getKey();
			chain.compare(entry.getValue(), another.getProperties().get(key));
		});
		return  chain.result();
	}
	
	@Override
	public String toString(){
		Gson gson = new Gson(); 
		return gson.toJson(keyValue);
	}	

}
