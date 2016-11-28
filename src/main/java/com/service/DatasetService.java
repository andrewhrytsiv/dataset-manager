package com.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.DatasetDAO;
import com.datasets.json.JSObject;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;
import com.entity.render.SimpleDatasetJsonRender;
import com.google.common.collect.Lists;
import com.util.Utility;

public class DatasetService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetService.class);
	
	@Autowired
	private DatasetDAO datasetDAO;
	
	public Dataset find(String datasetId){
		return datasetDAO.find(datasetId);
	}
	
	public boolean saveDataset(Dataset dataset, MetadataKeyValue metadata){
		try{
			if(datasetDAO.exist(dataset.getUuid())){
				datasetDAO.update(dataset);
				//need implement metadata updating 
			}else{
				datasetDAO.insert(dataset, metadata);
			}
		}catch(Exception ex){
			LOGGER.error(ex.getMessage(), ex);
			return false;
		}
		return true;
		
	}

	public List<SimpleDatasetJsonRender> findByUser(Integer userId) {
		List<SimpleDatasetJsonRender> simpleList = datasetDAO.findByUser(userId).stream().map(dataset -> {
			SimpleDatasetJsonRender dset = new SimpleDatasetJsonRender();
			dset.setId(dataset.getUuid().toString());
			dset.setUrl(dataset.getUrl());
			dset.setPersonal(dataset.isPersonal());
			dset.setSnapshotDate(dataset.getSnapshotDate().format(DateTimeFormatter.ofPattern(Utility.DATE_FORMAT)));
			return dset;
		}).collect(Collectors.toList());
		return simpleList;
	}
	
	public String findMetadata(String datasetId) {
		try {
			MetadataKeyValue metadata = datasetDAO.findMetadata(datasetId);
			JSObject jsObj = new JSObject(metadata.getKeyValue());
			return jsObj.toString();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
		return null;
	}
	public List<String> findAllMetadata() {
		List<String> list = Lists.newArrayList();
		List<MetadataKeyValue> metadataList = datasetDAO.findAllMetadata();
		for(MetadataKeyValue metadata : metadataList){
			try{
				String jsonMetadata = new JSObject(metadata.getKeyValue()).toString();
				list.add(jsonMetadata);
			}catch(Exception ex){
				LOGGER.error(ex.getMessage());
			}
		}
		return list;
	}
}
