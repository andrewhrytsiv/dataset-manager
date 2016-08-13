package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.UrlDataDAO;
import com.entity.UrlData;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;

public class UrlDataService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UrlDataService.class);
	
	@Autowired
	private UrlDataDAO urlDataDAO;
	
	public boolean insertUrlData(UrlData data) throws MalformedURLException, IOException{
		URL websiteData = new URL(data.getUrl());
		LOGGER.info("Start loading data from "+websiteData);
		InputStream in = websiteData.openStream();
		byte[] bytes = ByteStreams.toByteArray(in);
		urlDataDAO.insert(data, bytes);
		LOGGER.info(websiteData+" loaded in system");
		return true;
	}
	
	public List<UrlData> findAll(){
		return urlDataDAO.findAll();
	}
}
