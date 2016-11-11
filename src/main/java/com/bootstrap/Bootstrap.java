package com.bootstrap;

import static spark.Spark.*;
import static com.util.HTTPHelper.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.filter.ProtectedFilter;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.service.AuthenticationService;


import static spark.debug.DebugScreen.enableDebugScreen;

import java.util.Date;
import java.util.Map;

public class Bootstrap {
	
	private static final int PORT = 8090;
	public static final String API_CONTEXT = "/api";
 
    public static void main(String[] args) {
         port(PORT);
         //specify path to public directory
         externalStaticFileLocation(System.getProperty("app.home")+"/public");
         setFilters();
         initSpringConfigs();
         enableDebugScreen();
    }

	private static void setFilters() {
		before(API_CONTEXT + "/protected/*", new ProtectedFilter());
	}

	private static AnnotationConfigApplicationContext initSpringConfigs() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringAppConfig.class);
        context.register(SpringDatabaseConfig.class);
        context.refresh();
		return context;
	}
}