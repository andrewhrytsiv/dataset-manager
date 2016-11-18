package com.util;

import java.util.List;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import spark.Request;
import static com.util.AppConstants.*;

public class Utility {
	
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static boolean isNotEmptyValues(List<String> values){
		for(String value : values){
			if(Strings.isNullOrEmpty(value)){
				return false;
			}
		}
		return true;
	}
	
	public static Integer getUserId(Request request){
		return (Integer) request.raw().getAttribute(USER_ID);
	}
	
	public static boolean isJSONValid(String jsonString) {
		Gson gson = new Gson();
		try {
			gson.fromJson(jsonString, Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	} 
	
}
