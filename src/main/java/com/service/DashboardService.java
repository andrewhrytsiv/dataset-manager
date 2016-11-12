package com.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.datasets.parsers.XLSXParser;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

public class DashboardService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);
	
	@Autowired
	private DatasetService datasetService;
	
	public boolean saveDatasetFromXLSXFile(InputStream fileInStream, Context context){
		XLSXParser parser = new XLSXParser();
		try (ByteArrayInputStream input = new ByteArrayInputStream(ByteStreams.toByteArray(fileInStream))){
			String message = parser.read(input);
			if(!Strings.isNullOrEmpty(message)){
				context.withMessage(message);
			}
			String jsonBody = parser.buildJson();
			Map<String,String> metadataKeyValue = parser.getFileModel().getMetaData();
			UUID uuid = UUID.fromString(parser.getDatasetId());
			
			Dataset dataset = new Dataset();
			dataset.setUuid(uuid);
			dataset.setJsonData(jsonBody);
			dataset.setSnapshotDate(LocalDateTime.now());
			dataset.setOwnerId(context.getUserId());
			dataset.setUrl(context.getUrl());
			
			MetadataKeyValue metadata = new MetadataKeyValue();
			metadata.setUuid(uuid);
			metadata.setTable(Dataset.TABLE);
			metadata.setKeyValue(metadataKeyValue);
			
			datasetService.saveDataset(dataset, metadata);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
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
