package com.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.datasets.filemodels.xlsx.XLSXFileModel;
import com.datasets.parsers.XLSXParser;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.resource.DashboardResource;

public class DashboardService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);
	
	@Autowired
	private DatasetService datasetService;
	
	public boolean insertDataset(InputStream fileInStream, DSetContext context){
		XLSXParser parser = new XLSXParser();
		try (ByteArrayInputStream input = new ByteArrayInputStream(ByteStreams.toByteArray(fileInStream))){
			String message = parser.read(input);
			if(!Strings.isNullOrEmpty(message)){
				context.setMessage(message);
			}
			String jsonBody = parser.buildJson();
			Map<String,String> metadataKeyValue = parser.getFileModel().getMetaData();
			UUID uuid = UUID.fromString(parser.getDatasetId());
			Dataset dataset = new Dataset();
			dataset.setUuid(uuid);
			dataset.setJsonData(jsonBody);
			dataset.setSnapshotDate(LocalDateTime.now());
			dataset.setOwner(context.getUserName());
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
	
	public static class DSetContext{
		private String userName;
		private String type;
		private String message;
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
