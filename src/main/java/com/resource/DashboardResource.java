package com.resource;

import static com.util.AppConstants.*;
import static spark.Spark.*;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.bootstrap.Bootstrap.*;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.DashboardService;
import com.service.DatasetService;
import com.transformer.JsonTransformer;
import com.util.HTTP;
import com.util.Utility;

public class DashboardResource extends Resource{
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardResource.class);
	
	@Autowired
	private DashboardService dashboardService;
	
	@Autowired
	private DatasetService datasetService;
	
	public DashboardResource() {
		setupEndpoints();
	}

	protected void setupEndpoints() {
		
		get(API_CONTEXT + "/protected/dashboard/datasets", "application/json",
				(request, response) -> datasetService.findByUser(Utility.getUserId(request)),
				new JsonTransformer());
		
		post(API_CONTEXT + "/protected/dashboard/fileupload", (request, response) -> {
			MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
			request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
			Part file = request.raw().getPart("file"); // file is name of the upload form
			Integer userId =  Utility.getUserId(request);
			DashboardService.Context context = DashboardService.newContext()
					.withUserId(userId)
					.withType(request.queryParams("type"));
			if (dashboardService.saveDatasetFromXLSXFile(file.getInputStream(), context)) {
				response.status(HTTP.OK);
			} else {
				response.status(HTTP.INTERNAL_SERVER_ERROR);
				JsonObject  messageJson = new JsonObject();
				messageJson.addProperty(ERROR_MESSAGE, context.getMessage());
				return messageJson.toString();
			}
//			if(!Strings.isNullOrEmpty(context.getMessage())){
//				JsonObject  messageJson = new JsonObject();
//				messageJson.addProperty(ERROR_MESSAGE, context.getMessage());
//				return messageJson.toString();
//			}
			return EMPTY_RESPONSE;
		});
		
		post(API_CONTEXT + "/protected/dashboard/urlupload", "application/json", (request, response) -> {
			JsonObject info = new JsonParser().parse(request.body()).getAsJsonObject();
			String url = info.get(DATASET_URL).getAsString();
			String type = info.get(FILE_TYPE).getAsString();
			Integer userId = Utility.getUserId(request);
			URL websiteData = new URL(url);
			LOGGER.info("Start loading data from " + websiteData);
			InputStream fileStream = websiteData.openStream();
			DashboardService.Context context = DashboardService.newContext()
					.withUserId(userId)
					.withType(type)
					.withUrl(url);
			if (dashboardService.saveDatasetFromXLSXFile(fileStream, context)) {
				response.status(HTTP.OK);
			} else {
				response.status(HTTP.INTERNAL_SERVER_ERROR);
			}
			if(!Strings.isNullOrEmpty(context.getMessage())){
				JsonObject  messageJson = new JsonObject();
				messageJson.addProperty(ERROR_MESSAGE, context.getMessage());
				return messageJson.toString();
			}
			return EMPTY_RESPONSE;
		});
		
		get(API_CONTEXT + "/protected/dashboard/metadatafordatasets", "application/json", (request, response) -> {
			
			return EMPTY_RESPONSE;
		});
		
	}
}
