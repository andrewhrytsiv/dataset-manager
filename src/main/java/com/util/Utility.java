package com.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.common.base.Strings;

public class Utility {
	
	public static boolean isNotEmptyValues(List<String> values){
		for(String value : values){
			if(Strings.isNullOrEmpty(value)){
				return false;
			}
		}
		return true;
	}
	
}
