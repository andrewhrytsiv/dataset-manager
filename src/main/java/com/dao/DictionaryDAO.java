package com.dao;

import java.util.List;

import com.entity.Dictionary;

public interface DictionaryDAO {
	
	Dictionary findDictionary(String key);

	List<Dictionary> findDictionaries(String type);

	void insert(Dictionary dictionary);

	void insert(List<Dictionary> dictionaryList);
}
