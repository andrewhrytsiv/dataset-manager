package com.scheduler;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.service.DatasetService;

public class DatasetsScheduledService extends AbstractScheduledService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetsScheduledService.class);
	
	@Autowired
	private DatasetService datasetService;

	@Override
	protected void startUp() {
		System.out.println("DatasetsScheduledService start....");
	}
	
	@Override
	protected void shutDown() {
		System.out.println("DatasetsScheduledService shutdown...");
	}
	
	@Override
	protected void runOneIteration() throws Exception {
		try{
			Map<String, String>  idUrlMap = datasetService.getDatasetsUrlWithExpirationDate();
			for(Map.Entry<String, String> entry : idUrlMap.entrySet()){
				Runnable task = new DatasetUpdateTask(datasetService, entry.getKey(), entry.getValue());
				new Thread(task).start();
			}
		}catch(Exception ex){
			LOGGER.error("Can't start dataset update task.");
		}
		
	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedRateSchedule(0, 30, TimeUnit.SECONDS);
	}
	
}
