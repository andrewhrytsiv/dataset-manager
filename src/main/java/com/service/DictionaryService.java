package com.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.DictionaryDAO;
import com.entity.Dictionary;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

public class DictionaryService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryService.class);
	
	@Autowired
	private DictionaryDAO dictionaryDAO;
	
	public boolean saveDictionary(List<Dictionary> dictionaryList){
		try{
			List<Dictionary> updateList = Lists.newArrayList();
			List<Dictionary> insertList = Lists.newArrayList();
			Table<String,String,Boolean> existTable = dictionaryDAO.exist(dictionaryList);
			for(Dictionary dictionary : dictionaryList){
				if(existTable.contains(dictionary.getKey(), dictionary.getNullTypeAsString())){
					updateList.add(dictionary);
				}else{
					insertList.add(dictionary);
				}
			}
			dictionaryDAO.insert(insertList);
			dictionaryDAO.update(updateList);
		}catch(Exception ex){
			LOGGER.error(ex.getMessage(), ex);
			return false;
		}
		return true;
	}
	
	public List<Dictionary> findAll(){
		return dictionaryDAO.findAll();
	}
}
