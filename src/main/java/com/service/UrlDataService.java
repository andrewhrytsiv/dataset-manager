package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.UrlDataDAO;
import com.entity.UrlData;
import com.google.common.io.ByteStreams;
import com.resource.UrlDataResource;

public class UrlDataService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(UrlDataService.class);
	
	@Autowired
	private UrlDataDAO urlDataDAO;
	
	public boolean insertUrlData(UrlData data) throws MalformedURLException, IOException{
		URL websiteData = new URL(data.getUrl());
		InputStream in = websiteData.openStream();
		byte[] bytes = ByteStreams.toByteArray(in);
		data.setType("csv");
		urlDataDAO.insert(data, bytes);
//		Scanner scanner = new Scanner(in);
//		while (scanner.hasNext()) {
//			System.out.println(scanner.next());
//		}
		return true;
	}
}
