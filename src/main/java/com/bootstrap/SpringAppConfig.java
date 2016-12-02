package com.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.resource.AuthenticationResource;
import com.resource.DashboardResource;
import com.resource.DatasetResource;
import com.scheduler.DatasetsScheduledService;
import com.service.AuthenticationService;
import com.service.DashboardService;
import com.service.DatasetService;
import com.service.DictionaryService;

@Configuration
public class SpringAppConfig {
	
	@Bean
    public AuthenticationResource authenticationResource() {
        return new AuthenticationResource();
    }
	@Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService();
    }
	
	@Bean
    public DatasetResource datasetResource() {
        return new DatasetResource();
    }
	@Bean
    public DatasetService datasetService() {
        return new DatasetService();
    }
	
	@Bean
	public DashboardResource dashboardResource(){
		return new DashboardResource();
	}
	@Bean
	public DashboardService dashboardService(){
		return new DashboardService();
	}
	
	@Bean
	public DictionaryService dictionaryService(){
		return new DictionaryService();
	}
	
	@Bean
	public DatasetsScheduledService datasetsScheduledService(){
		return new DatasetsScheduledService();
	}
}
