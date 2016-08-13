package com.dao;

import java.util.List;

import com.entity.UrlData;

public interface UrlDataDAO {
	
	void insert(UrlData data, byte[] bytes);
	
	List<UrlData> findAll();
}
