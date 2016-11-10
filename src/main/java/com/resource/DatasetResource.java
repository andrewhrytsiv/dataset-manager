package com.resource;

import static com.util.HTTPHelper.EMAIL;
import static com.util.HTTPHelper.EMPTY_RESPONSE;
import static com.util.HTTPHelper.OK_STATUS;
import static com.util.HTTPHelper.PASSWORD;
import static com.util.HTTPHelper.UNAUTHORIZED_STATUS;
import static com.util.HTTPHelper.USER_NAME;
import static spark.Spark.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.service.DatasetService;
import static com.util.HTTPHelper.*;
import com.util.JsonTransformer;
import com.util.Utility;



public class DatasetResource extends Resource{
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetResource.class);
	
	@Autowired
	private DatasetService datasetService;
	
	public DatasetResource(){
		setupEndpoints();
	}
	
	protected void setupEndpoints() {
		
	}
}
