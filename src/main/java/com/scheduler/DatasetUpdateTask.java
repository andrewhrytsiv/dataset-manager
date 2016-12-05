package com.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.service.DatasetService;

public class DatasetUpdateTask implements Runnable{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetUpdateTask.class);
	
	private String taskName;
	private DatasetService datasetService;
	
	public DatasetUpdateTask(String taskName, DatasetService datasetService){
		this.datasetService = datasetService;
	}

	@Override
	public void run() {
		LOGGER.info("Start task#"+taskName);
//		select * from dataset to update 
		datasetService.updateDatasetFromUrl("datasetId", "url");
		LOGGER.info("Finish task#"+taskName);
	}

}
