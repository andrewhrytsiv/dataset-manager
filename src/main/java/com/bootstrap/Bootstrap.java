package com.bootstrap;

import static spark.Spark.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.filter.ProtectedFilter;
import com.scheduler.DatasetsScheduledService;

public class Bootstrap {
	
	private static final int PORT = 8090;
	public static final String API_CONTEXT = "/api";
	public static AnnotationConfigApplicationContext springContext;
 
    public static void main(String[] args) {
         port(PORT);
         //specify path to public directory
         externalStaticFileLocation(System.getProperty("app.home")+"/public");
         setFilters();
         initSpringConfigs();
         startAsyncServices();
    }

	private static void startAsyncServices() {
		springContext.getBean(DatasetsScheduledService.class).startAsync();
	}

	private static void setFilters() {
		before(API_CONTEXT + "/protected/*", new ProtectedFilter());
	}

	private static AnnotationConfigApplicationContext initSpringConfigs() {
		springContext = new AnnotationConfigApplicationContext();
		springContext.register(SpringAppConfig.class);
		springContext.register(SpringDatabaseConfig.class);
		springContext.refresh();
		return springContext;
	}
}