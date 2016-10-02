package com.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.service.AuthenticationService;
import com.service.DashboardService;
import com.service.UrlDataService;

@Configuration
public class SpringAppConfig {
	@Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService();
    }
	@Bean
    public UrlDataService urlDataService() {
        return new UrlDataService();
    }
	@Bean
	public DashboardService dashboardService(){
		return new DashboardService();
	}
}
