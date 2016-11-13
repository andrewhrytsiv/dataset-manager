package com.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.DatasetDAO;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;
import com.entity.SimpleDatasetRender;
import com.util.Utility;

public class DatasetService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetService.class);
	
	@Autowired
	private DatasetDAO datasetDAO;
	
	public Dataset find(UUID datasetId){
		return datasetDAO.find(datasetId);
	}
	
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

	public List<SimpleDatasetRender> findByUser(Integer userId) {
		List<SimpleDatasetRender> simpleList = datasetDAO.findByUser(userId).stream().map(dataset -> {
			SimpleDatasetRender dset = new SimpleDatasetRender();
			dset.setId(dataset.getUuid().toString());
			dset.setUrl(dataset.getUrl());
			dset.setPersonal(dataset.isPersonal());
			dset.setSnapshotDate(dataset.getSnapshotDate().format(DateTimeFormatter.ofPattern(Utility.DATE_FORMAT)));
			return dset;
		}).collect(Collectors.toList());
		return simpleList;
	}
}
