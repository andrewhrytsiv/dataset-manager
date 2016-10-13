package com.bootstrap;

 
import static spark.Spark.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.resource.AuthenticationResource;
import com.resource.DashboardResource;
import com.resource.UrlDataResource;
import com.service.AuthenticationService;
import com.service.DashboardService;
import com.service.UrlDataService;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Bootstrap {
	
	private static final int PORT = 8090;
	public static final String API_CONTEXT = "/api";
 
    public static void main(String[] args) {
//    	 Spark.setSecure("deploy/private/localhost.jks", "andrew", null, null); //https set up
         port(PORT);
         //run from eclipse
         //staticFileLocation("./public");
         //run from jar
         externalStaticFileLocation(System.getProperty("app.home")+"/public");
         AnnotationConfigApplicationContext context = initSpringConfigs();
         new AuthenticationResource(context.getBean(AuthenticationService.class));
         new UrlDataResource(context.getBean(UrlDataService.class));
         new DashboardResource(context.getBean(DashboardService.class));
         enableDebugScreen();
    }

	private static AnnotationConfigApplicationContext initSpringConfigs() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringAppConfig.class);
        context.register(SpringDatabaseConfig.class);
        context.refresh();
		return context;
	}
}