package com.dao;

import java.io.InputStream;

import com.entity.UrlData;

public interface UrlDataDAO {
	
	public void insert(UrlData data);

	void insert(UrlData data, byte[] bytes);
	
}
