package com.resource;

import static spark.Spark.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bootstrap.Bootstrap;
import com.entity.User;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.AuthenticationService;
import static com.util.AppConstants.*;

import com.util.HTTP;
import com.util.Utility;

public class AuthenticationResource extends Resource{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);
	
	@Autowired
	private AuthenticationService authService;
	
	public AuthenticationResource(){
		setupEndpoints();
	}

	protected void setupEndpoints() {
		
		post(Bootstrap.API_CONTEXT + "/register", "application/json", (request, response) -> {
			JsonObject  userObject = new JsonParser().parse(request.body()).getAsJsonObject();
			String email = userObject.get(EMAIL).getAsString();
			String username = userObject.get(USER_NAME).getAsString();
			String password = userObject.get(PASSWORD).getAsString();
			User newUser = new User(email, username, User.hash(password));
			if (Utility.isNotEmptyValues(Lists.newArrayList(email, username, password)) && authService.registrer(newUser)) {
				LOGGER.info("New user was regiter "+newUser);
				JsonObject body = getAuthenticateResponseJson(newUser);
				response.status(HTTP.OK);
				return body.toString();
			} else {
				response.status(HTTP.UNAUTHORIZED);
			}
            return EMPTY_RESPONSE;
        });
		
		post(Bootstrap.API_CONTEXT + "/login", "application/json",(request, response) -> {
			JsonObject  userObject = new JsonParser().parse(request.body()).getAsJsonObject();
			JsonObject  messageJson = new JsonObject();
			String email = userObject.get(EMAIL).getAsString();
			String password = userObject.get(PASSWORD).getAsString();
			User user = authService.getUser(email);
			if(user != null && User.hash(password).equals(user.getPassword())){
				LOGGER.info(user.toString()+" was login. ");
				JsonObject body = getAuthenticateResponseJson(user);
				response.status(HTTP.OK);
				return body.toString();
			}else{
				String message = "";
				if(user == null){
					message = "User with "+email+" doesn't exist!";
				}else{
					message = "Wrong password!";
				}
				LOGGER.error("Failed to login: " + message);
				messageJson.addProperty(ERROR_MESSAGE, message);
				response.status(HTTP.UNAUTHORIZED);
			}
			return messageJson.toString();
		});
		
		post(Bootstrap.API_CONTEXT + "/validatetocken", "application/json",(request, response) -> {
			JsonObject  param = new JsonParser().parse(request.body()).getAsJsonObject();
			String tocken = param.get(ACCESS_TOKEN).getAsString();
			if(authService.validateTocken(tocken)){
				response.status(HTTP.OK);
			}else{
				response.status(HTTP.UNAUTHORIZED);
			}
			return EMPTY_RESPONSE;
		});
		
	}

	private JsonObject getAuthenticateResponseJson(User user) {
		String tocken = authService.generateJWT(user.getId());
		JsonObject body = new JsonObject();
		body.addProperty(USER_NAME, user.getUserName());
		body.addProperty(ACCESS_TOKEN, tocken);
		return body;
	} 
	
}
