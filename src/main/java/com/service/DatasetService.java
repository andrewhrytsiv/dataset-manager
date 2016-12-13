package com.service;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.DatasetDAO;
import com.datasets.json.JSObject;
import com.datasets.parsers.JSONDatasetsParser;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;
import com.entity.render.SimpleDatasetJsonRender;
import com.google.common.collect.Lists;
import com.util.Period;
import com.util.Utility;
import static com.util.AppConstants.*;

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
				//need implement metadata updating maybe. Need discuss this with Boldak
			}else{
				datasetDAO.insert(dataset, metadata);
			}
		}catch(Exception ex){
			LOGGER.error(ex.getMessage(), ex);
			return false;
		}
		return true;
	}
	
	public Map<String, String> getDatasetsUrlWithExpirationDate(){
		return datasetDAO.getDatasetsUrlWithExpirationDate();
	}
	
	public boolean updateDatasetFromUrl(String datasetId, String url){
		try{
			URL websiteData = new URL(url);
			LOGGER.info(START_LOADING_DATA_FROM + websiteData);
			InputStream fileStream = websiteData.openStream();
			JSONDatasetsParser parser = new JSONDatasetsParser();
			parser.read(fileStream);
			String datasetJson = parser.parseDataset();
			
			Dataset dataset = new Dataset();
			dataset.setUuid(datasetId);
			dataset.setJsonData(datasetJson);
			dataset.setSnapshotDate(LocalDateTime.now());
			
			datasetDAO.updateDataOnly(dataset);
		}catch(Exception ex){
			LOGGER.error("Error when loading from "+url, ex);
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
			if(dataset.getNextUpdateInMinutes() != null){
				dset.setPeriod(new Period(dataset.getNextUpdateInMinutes()));
			}
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
	
	public List<String> findMetadataByKey(String key, String value){
		List<String> list = Lists.newArrayList();
		List<MetadataKeyValue> metadataList = datasetDAO.findMetadataByKey(key, value);
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
	
	public boolean updateDatasetInterval(String datasetId, int minutes) {
		try{
			datasetDAO.updateDatasetInterval(datasetId, minutes);
		}catch(Exception ex){
			LOGGER.error("Can't update interval for datasetId="+datasetId);
			return false;
		}
		return true;
	}
	
	public static Context newContext(){
		return new Context();
	}
	
	public static class Context{
		private Integer userId;
		private String message;
		
		private Context(){}
		
		public Integer getUserId() {
			return userId;
		}
		public Context withUserId(Integer userId) {
			this.userId = userId;
			return this;
		}
		
		public String getMessage() {
			return message;
		}
		public Context withMessage(String message) {
			this.message = message;
			return this;
		}
	}
	
}
