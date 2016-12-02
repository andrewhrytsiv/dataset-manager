package com.bootstrap;

 
import static spark.Spark.*;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.filter.ProtectedFilter;
import com.scheduler.DatasetsScheduledService;


public class Bootstrap {
	
	private static final int DEFAULT_PORT = 4567;
	public static final String API_CONTEXT = "/api";
	public static AnnotationConfigApplicationContext springContext;
 
    public static void main(String[] args) throws URISyntaxException {
         port(getHerokuAssignedPort());
         externalStaticFileLocation(getPublicFolderPath());
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
    
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return DEFAULT_PORT;
    }
    
    private static String getPublicFolderPath() throws URISyntaxException{
    	CodeSource codeSource = Bootstrap.class.getProtectionDomain().getCodeSource();
    	File jarFile = new File(codeSource.getLocation().toURI().getPath());
    	String jarDir = jarFile.getParentFile().getPath();
    	return jarDir + File.separator + "public";
    }

	private static AnnotationConfigApplicationContext initSpringConfigs() {
		springContext = new AnnotationConfigApplicationContext();
		springContext.register(SpringAppConfig.class);
		springContext.register(SpringDatabaseConfig.class);
		springContext.refresh();
		return springContext;
	}
}