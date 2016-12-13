package com.resource;

import com.util.HTTP;
import com.util.MediaType;

import static spark.Spark.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.bootstrap.Bootstrap;
import com.entity.Dataset;
import com.google.common.base.Joiner;
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
		get(Bootstrap.API_CONTEXT + "/dataset/get/:datasetID", MediaType.APPLICATION_JSON, (request, response) -> {
			Dataset dataset = datasetService.find(request.params(":datasetID"));
			if(dataset != null){
				response.status(HTTP.OK);
				return dataset.getJsonData();
			}else{
				response.status(HTTP.NOT_FOUND);
			}
			return EMPTY_RESPONSE;
		});
		
		post(Bootstrap.API_CONTEXT + "/dataset/get/:datasetID", MediaType.APPLICATION_JSON, (request, response) -> {
			Dataset dataset = datasetService.find(request.params(":datasetID"));
			if(dataset != null){
				response.status(HTTP.OK);
				return dataset.getJsonData();
			}else{
				response.status(HTTP.NOT_FOUND);
			}
			return EMPTY_RESPONSE;
		});
		
		get(Bootstrap.API_CONTEXT + "/metadata/items", MediaType.APPLICATION_JSON, (request, response) -> {
			String tagParam = request.queryMap().get("tag").value();
			String valueParam = request.queryMap().get("value").value();
			LOGGER.info("tag="+tagParam+" value="+valueParam);
			List<String> result = datasetService.findMetadataByKey(tagParam, valueParam);
			String jsonArray = "[" + Joiner.on(",").join(result) + "]";
			response.status(HTTP.OK);
			return jsonArray;
		});
		
		post(Bootstrap.API_CONTEXT + "/metadata/items", MediaType.APPLICATION_JSON, (request, response) -> {
			String tagParam = request.queryMap().get("tag").value();
			String valueParam = request.queryMap().get("value").value();
			LOGGER.info("tag="+tagParam+" value="+valueParam);
			List<String> result = datasetService.findMetadataByKey(tagParam, valueParam);
			String jsonArray = "[" + Joiner.on(",").join(result) + "]";
			response.status(HTTP.OK);
			return jsonArray;
		});
	}
}
