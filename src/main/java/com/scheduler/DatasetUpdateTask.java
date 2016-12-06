package com.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.service.DatasetService;

public class DatasetUpdateTask implements Runnable{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetUpdateTask.class);
	
	private String taskName;
	private DatasetService datasetService;
	private String datasetId;
	private String url;
	
	public DatasetUpdateTask(DatasetService datasetService, String datasetId, String url){
		this.datasetService = datasetService;
		this.datasetId = datasetId;
		this.url = url;
		this.taskName = "DatasetUpdateTask(id->" + datasetId + ",url->" + url + ")";
	}

	@Override
	public void run() {
		LOGGER.info("Start "+taskName);
		datasetService.updateDatasetFromUrl(datasetId, url);
		LOGGER.info("Finish "+taskName);
	}

}
