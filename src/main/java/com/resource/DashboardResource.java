package com.resource;

import static com.util.HTTPHelper.EMPTY_RESPONSE;
import static com.util.HTTPHelper.NOT_FOUND;
import static com.util.HTTPHelper.OK_STATUS;
import static com.util.HTTPHelper.URL;
import static spark.Spark.*;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bootstrap.Bootstrap;
import com.entity.UrlData;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.DashboardService;
import com.util.JsonTransformer;

public class DashboardResource extends Resource{
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardResource.class);
	
	private DashboardService dashboardService;
	
	
	public DashboardResource(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
		setupEndpoints();
	}

	protected void setupEndpoints() {
		
		post(Bootstrap.API_CONTEXT + "/dashboard/fileupload", (request, response) -> {
			 MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
			 request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
			 Part file = request.raw().getPart("file"); //file is name of the upload form
			 System.out.println(file.getContentType());
			 if (dashboardService.insertDataset(file.getInputStream(), "xlsx")) {
					response.status(OK_STATUS);
					return "some data";
				} else {
					response.status(NOT_FOUND);
				}
            return EMPTY_RESPONSE;
        });
	}
}
