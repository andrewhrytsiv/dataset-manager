package com.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.datasets.parsers.JSONDatasetsParser;
import com.datasets.parsers.JSONDictionaryParser;
import com.datasets.parsers.XLSXParser;
import com.entity.Dataset;
import com.entity.Dictionary;
import com.entity.MetadataKeyValue;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.util.AppConstants;
import com.util.HTTP;
import com.util.Pair;
import com.util.Utility;
import static com.util.AppConstants.*;

public class DashboardService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);
	
	@Autowired
	private DatasetService datasetService;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	public boolean saveDatasetFromFile(InputStream fileStream, Context context){
		boolean saved = false;
		switch (context.getType()) {
		case "xlsx":
			saved = saveDatasetFromXLSXFile(fileStream, context);
			break;
		case "json_datasets":
			saved = saveDatasetsFromJsonFile(fileStream, context);
			break;
		case "json_dictionary":
			saved = saveDictionaryFromJsonFile(fileStream, context);
			break;
		}
		return saved;
	}
	
	public boolean saveDatasetsFromJsonFile(InputStream fileStream, Context context){
		try {
			JSONDatasetsParser parser = new JSONDatasetsParser();
			parser.read(fileStream);
			List<Pair<String, LinkedHashMap<String, String>>> datasetsList = parser.parseDatasetsWithMetadata();
			String datasetDownloadApiLink = parser.getFileModel().getSourceHostDownloadLink();
			for(Pair<String, LinkedHashMap<String, String>> dsetPair : datasetsList){
				try{
					String datasetJson = dsetPair.getLeft();
					LinkedHashMap<String, String> metadataKeyValue = dsetPair.getRight();
					String uuid = metadataKeyValue.get(DATASET_DOT_ID);
					Dataset dataset = new Dataset();
					dataset.setUuid(uuid);
					dataset.setJsonData(datasetJson);
					dataset.setSnapshotDate(LocalDateTime.now());
					dataset.setOwnerId(context.getUserId());
					dataset.setUrl(HTTP.https + datasetDownloadApiLink + uuid);
					dataset.setNextUpdateInMinutes(AppConstants.ONE_WEEK_MINUTES);
					
					MetadataKeyValue metadata = new MetadataKeyValue();
					metadata.setUuid(uuid);
					metadata.setTable(Dataset.TABLE);
					metadata.setKeyValue(metadataKeyValue);
					
					datasetService.saveDataset(dataset, metadata);
				}catch(Exception ex){
					LOGGER.error("Dataset saving problam");
				}
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean saveDictionaryFromJsonFile(InputStream fileStream, Context context){
		try{
			JSONDictionaryParser parser = new JSONDictionaryParser();
			parser.read(fileStream);
			List<Dictionary> dictionaryList = parser.parseDictionary();
			dictionaryService.saveDictionary(dictionaryList);
		}catch(Exception ex){
			LOGGER.error("Dictionary saving problam");
		}
		return true;
	}
	
	public boolean saveDatasetFromXLSXFile(InputStream fileInStream, Context context){
		XLSXParser parser = new XLSXParser();
		try (ByteArrayInputStream input = new ByteArrayInputStream(ByteStreams.toByteArray(fileInStream))){
			String message = parser.read(input);
			if(!Strings.isNullOrEmpty(message)){
				context.withMessage(message);
			}
			String jsonBody = parser.buildJson();
			jsonBody = Utility.getPrettyJsonWithEscapedCharacters(jsonBody);
			if(jsonBody == null){
				String notValidMsg = "Not valid json";
				context.withMessage(notValidMsg);
				LOGGER.error(notValidMsg);
				return false;
			}
			Map<String,String> metadataKeyValue = parser.getFileModel().getMetaData();
			String uuid = parser.getDatasetId();
			
			Dataset dataset = new Dataset();
			dataset.setUuid(uuid);
			dataset.setJsonData(jsonBody);
			dataset.setSnapshotDate(LocalDateTime.now());
			dataset.setOwnerId(context.getUserId());
			dataset.setUrl(context.getUrl());
			if(context.getUrl() != null){
				dataset.setNextUpdateInMinutes(AppConstants.ONE_WEEK_MINUTES);
			}
			
			MetadataKeyValue metadata = new MetadataKeyValue();
			metadata.setUuid(uuid);
			metadata.setTable(Dataset.TABLE);
			metadata.setKeyValue(metadataKeyValue);
			
			datasetService.saveDataset(dataset, metadata);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}finally {
			Closeables.closeQuietly(fileInStream);
		}
		return true;
	}
	
	public static Context newContext(){
		return new Context();
	}
	
	public static class Context{
		private Integer userId;
		private String type;
		private String url;
		private String message;
		
		private Context(){}
		
		public Integer getUserId() {
			return userId;
		}
		public Context withUserId(Integer userId) {
			this.userId = userId;
			return this;
		}
		public String getType() {
			return type;
		}
		public Context withType(String type) {
			this.type = type;
			return this;
		}
		public String getMessage() {
			return message;
		}
		public Context withMessage(String message) {
			this.message = message;
			return this;
		}

		public String getUrl() {
			return url;
		}

		public Context withUrl(String url) {
			this.url = url;
			return this;
		}
	}
}
