package com.dao;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

import com.entity.Dataset;
import com.entity.MetadataKeyValue;

public interface DatasetDAO {
	
	void insert(Dataset dataset,MetadataKeyValue metadata) throws DataAccessException, SQLException;
	
	Dataset find(String uuid);
}
