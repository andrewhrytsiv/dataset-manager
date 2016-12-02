package com.service;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.dao.DatasetDAO;
import com.datasets.json.JSObject;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;
import com.entity.render.SimpleDatasetJsonRender;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
	
	@SuppressWarnings("unchecked")
	public boolean saveDataset(String json, Context context){
		try{
			JsonParser jsonParser = new JsonParser();
			JsonElement metadataJson = jsonParser.parse(json).getAsJsonObject().get("metadata");
			Resource resource = new ClassPathResource("wdc-flat.js");
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			engine.eval(new FileReader(resource.getFile()));
			Invocable invocable = (Invocable) engine;
			LinkedHashMap<String, String> metadataKeyValue = (LinkedHashMap<String, String>) invocable.invokeFunction("json2flat", metadataJson.toString());
			String uuid = metadataKeyValue.get(DATASET_DOT_ID);
			
			Dataset dataset = new Dataset();
			dataset.setUuid(uuid);
			dataset.setJsonData(json);
			dataset.setSnapshotDate(LocalDateTime.now());
			dataset.setOwnerId(context.getUserId());

			MetadataKeyValue metadata = new MetadataKeyValue();
			metadata.setUuid(uuid);
			metadata.setTable(Dataset.TABLE);
			metadata.setKeyValue(metadataKeyValue);
			
			return saveDataset(dataset, metadata);
		}catch(Exception ex){
			LOGGER.error(ex.getMessage(), ex);
			return false;
		}
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
