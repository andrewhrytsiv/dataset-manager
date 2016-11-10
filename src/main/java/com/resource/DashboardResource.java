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
import org.springframework.beans.factory.annotation.Autowired;

import com.bootstrap.Bootstrap;
import com.entity.Dataset;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.DashboardService;
import com.util.JsonTransformer;

public class DashboardResource extends Resource{
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardResource.class);
	
	@Autowired
	private DashboardService dashboardService;
	
	public DashboardResource() {
		setupEndpoints();
	}

	protected void setupEndpoints() {
		
		post(Bootstrap.API_CONTEXT + "/protected/dashboard/fileupload", (request, response) -> {
			 MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
			 request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
			 Part file = request.raw().getPart("file"); //file is name of the upload form
			 System.out.println(file.getContentType());
			 DashboardService.DSetContext context = new DashboardService.DSetContext();
			 context.setUserName("Andrew");
			 if (dashboardService.insertDataset(file.getInputStream(), context)) {
					response.status(OK_STATUS);
					return "some data";
				} else {
					response.status(NOT_FOUND);
				}
            return EMPTY_RESPONSE;
        });
	}
}
