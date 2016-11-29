package com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;

import com.entity.Dataset;
import com.entity.Dictionary;
import com.entity.MetadataKeyValue;

public interface DatasetDAO {
	
	boolean exist(String datasetId);
	
	Dataset find(String datasetId);
	
	void insert(Dataset dataset,MetadataKeyValue metadata) throws DataAccessException, SQLException;

	void update(Dataset dataset) throws DataAccessException, SQLException;

	List<Dataset> findByUser(Integer userId);

	MetadataKeyValue findMetadata(String datasetId);

	List<MetadataKeyValue> findAllMetadata();

	Dictionary findDictionary(String key);

	List<Dictionary> findDictionaries(String type);

	void insert(Dictionary dictionary);

	void insert(List<Dictionary> dictionaryList);

}
