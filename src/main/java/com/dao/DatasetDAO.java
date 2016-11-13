package com.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;

import com.entity.Dataset;
import com.entity.MetadataKeyValue;

public interface DatasetDAO {
	
	boolean exist(UUID datasetId);
	
	Dataset find(UUID datasetId);
	
	void insert(Dataset dataset,MetadataKeyValue metadata) throws DataAccessException, SQLException;

	void update(Dataset dataset) throws DataAccessException, SQLException;

	List<Dataset> findByUser(Integer userId);

}
