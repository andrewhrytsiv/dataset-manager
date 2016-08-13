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

import com.bootstrap.Bootstrap;
import com.entity.UrlData;
import com.entity.User;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.UrlDataService;
import static com.util.HTTPHelper.*;

import com.util.JsonTransformer;
import com.util.Utility;



public class UrlDataResource {
	private final static Logger LOGGER = LoggerFactory.getLogger(UrlDataResource.class);
	
	private UrlDataService urlDataService;
	
	public UrlDataResource(UrlDataService dataUrlService){
		this.urlDataService = dataUrlService;
		setupEndpoints();
	}
	
	private void setupEndpoints() {
		
		get(Bootstrap.API_CONTEXT + "/url_data", "application/json", (request, response) -> urlDataService.findAll(), new JsonTransformer());
		
		post(Bootstrap.API_CONTEXT + "/url_data", "application/json", (request, response) -> {
			JsonObject  userObject = new JsonParser().parse(request.body()).getAsJsonObject();
			String url = userObject.get(URL).getAsString();
			UrlData newDataUrl = new UrlData(url);
			newDataUrl.setType("csv");
			if (urlDataService.insertUrlData(newDataUrl)) {
				response.status(OK_STATUS);
				return "some data";
			} else {
				response.status(NOT_FOUND);
			}
            return EMPTY_RESPONSE;
        });
	}
}
