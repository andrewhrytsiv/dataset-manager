package com.bootstrap;

import static spark.Spark.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Bootstrap {
	
	private static final int PORT = 8090;
	public static final String API_CONTEXT = "/api";
 
    public static void main(String[] args) {
         port(PORT);
         //specify path to public directory
         externalStaticFileLocation(System.getProperty("app.home")+"/public");
         initSpringConfigs();
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