package com.resource;

import com.util.HTTP;
import static spark.Spark.*;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.bootstrap.Bootstrap;
import com.entity.Dataset;
import com.service.DatasetService;
import static com.util.AppConstants.*;

public class DatasetResource extends Resource{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetResource.class);
	
	@Autowired
	private DatasetService datasetService;
	
	public DatasetResource(){
		setupEndpoints();
	}
	
	protected void setupEndpoints() {
		get(Bootstrap.API_CONTEXT + "/dataset/get/:datasetID", "application/json", (request, response) -> {
			Dataset dataset = datasetService.find(request.params(":datasetID"));
			if(dataset != null){
				response.status(HTTP.OK);
				return dataset.getJsonData();
			}else{
				response.status(HTTP.NOT_FOUND);
			}
			return EMPTY_RESPONSE;
		});
		
		post(Bootstrap.API_CONTEXT + "/dataset/get/:datasetID", "application/json", (request, response) -> {
			Dataset dataset = datasetService.find(request.params(":datasetID"));
			if(dataset != null){
				response.status(HTTP.OK);
				return dataset.getJsonData();
			}else{
				response.status(HTTP.NOT_FOUND);
			}
			return EMPTY_RESPONSE;
		});
	}
}
