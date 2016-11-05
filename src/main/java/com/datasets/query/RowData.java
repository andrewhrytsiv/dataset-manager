package com.datasets.query;

import java.util.Map;

import com.google.common.base.Objects;
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
			String thisValue = entry.getValue();
			chain.compare(thisValue, another.getProperties().get(key));
		});
		return  chain.result();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}  
	    final RowData another = (RowData) obj;  
		boolean isEqual = true;
		for(Map.Entry<String, String> entry : keyValue.entrySet()){
			String key = entry.getKey();
			String thisValue = entry.getValue();
			String anotherValue = another.getProperties().get(key);
			isEqual = isEqual && Objects.equal(thisValue, anotherValue);
		}
		return isEqual;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		for(Map.Entry<String, String> entry : keyValue.entrySet()){
			hashCode += Objects.hashCode(entry.getValue());
		}
		return hashCode;
	} 
	
	@Override
	public String toString(){
		Gson gson = new Gson(); 
		return gson.toJson(keyValue);
	}	

}
