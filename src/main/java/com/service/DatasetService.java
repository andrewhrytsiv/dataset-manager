package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dao.DatasetDAO;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;

@Component
public class DatasetService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetService.class);
	
	@Autowired
	private DatasetDAO datasetDAO;
	
	public boolean saveDataset(Dataset dataset, MetadataKeyValue metadata){
		try{
			if(datasetDAO.exist(dataset.getUuid())){
				datasetDAO.update(dataset);
			}else{
				datasetDAO.insert(dataset, metadata);
			}
		}catch(Exception ex){
			LOGGER.error(ex.getMessage(), ex);
			return false;
		}
		return true;
		
	}
}
