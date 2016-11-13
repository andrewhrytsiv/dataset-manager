package com.resource;

import static com.util.HTTPHelper.EMAIL;
import static com.util.HTTPHelper.EMPTY_RESPONSE;
import static com.util.HTTPHelper.OK_STATUS;
import static com.util.HTTPHelper.PASSWORD;
import static com.util.HTTPHelper.UNAUTHORIZED_STATUS;
import static com.util.HTTPHelper.USER_NAME;
import static spark.Spark.*;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.bootstrap.Bootstrap;
import com.entity.Dataset;
import com.service.DatasetService;
import static com.util.HTTPHelper.*;

public class DatasetResource extends Resource{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetResource.class);
	
	@Autowired
	private DatasetService datasetService;
	
	public DatasetResource(){
		setupEndpoints();
	}
	
	protected void setupEndpoints() {
		get(Bootstrap.API_CONTEXT + "/dataset/get/:datasetID", "application/json", (request, response) -> {
			Dataset dataset = datasetService.find(UUID.fromString(request.params(":datasetID")));
			if(dataset != null){
				response.status(OK_STATUS);
				return dataset.getJsonData();
			}else{
				response.status(NOT_FOUND);
			}
			return EMPTY_RESPONSE;
		});
		
		post(Bootstrap.API_CONTEXT + "/dataset/get/:datasetID", "application/json", (request, response) -> {
			Dataset dataset = datasetService.find(UUID.fromString(request.params(":datasetID")));
			if(dataset != null){
				response.status(OK_STATUS);
				return dataset.getJsonData();
			}else{
				response.status(NOT_FOUND);
			}
			return EMPTY_RESPONSE;
		});
	}
}
