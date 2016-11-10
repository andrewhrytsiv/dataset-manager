package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dao.DatasetDAO;
import com.dao.sql.DatasetDAOSql;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;

@Component
public class DatasetService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetService.class);
	
	@Autowired
	private DatasetDAO datasetDAO;
	
	public boolean saveDataset(Dataset dataset, MetadataKeyValue metadata){
		try{
			//check if exist then update
			datasetDAO.insert(dataset, metadata);
		}catch(Exception ex){
			LOGGER.error(ex.getMessage(), ex);
			return false;
		}
		return true;
		
	}
	
//	public boolean insertUrlData(Dataset data) throws MalformedURLException, IOException{
//		URL websiteData = new URL(data.getUrl());
//		LOGGER.info("Start loading data from "+websiteData);
//		InputStream in = websiteData.openStream();
//		byte[] bytes = ByteStreams.toByteArray(in);
//		urlDataDAO.insert(data, bytes);
//		LOGGER.info(websiteData+" loaded in system");
//		return true;
//	}
	
}
