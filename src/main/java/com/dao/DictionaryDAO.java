package com.dao;

import java.util.List;

import com.entity.Dictionary;
import com.google.common.collect.Table;

public interface DictionaryDAO {
	
	Table<String,String,Boolean> exist(List<Dictionary> dictionaryList);
	
	Dictionary find(String key, String typ);
	
	List<Dictionary> findDictionariesByKey(String key);

	List<Dictionary> findDictionariesByType(String type);

	void insert(Dictionary dictionary);

	void insert(List<Dictionary> dictionaryList);

	void update(Dictionary dictionary);
	
	void update(List<Dictionary> dictionaryList);

}
