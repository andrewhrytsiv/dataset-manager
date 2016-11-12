package com.bootstrap;

 
import static spark.Spark.*;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.filter.ProtectedFilter;


public class Bootstrap {
	
	private static final int DEFAULT_PORT = 4567;
	public static final String API_CONTEXT = "/api";
 
    public static void main(String[] args) throws URISyntaxException {
         port(getHerokuAssignedPort());
         externalStaticFileLocation(getPublicFolderPath());
         setFilters();
         initSpringConfigs();
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
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringAppConfig.class);
        context.register(SpringDatabaseConfig.class);
        context.refresh();
		return context;
	}
}