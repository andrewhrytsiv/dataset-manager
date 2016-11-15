package com.filter;

import com.util.HTTP;
import static spark.Spark.halt;
import static com.util.AppConstants.*;

import java.time.LocalDateTime;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.service.AuthenticationService;

import spark.Filter;
import spark.Request;
import spark.Response;

public class ProtectedFilter implements Filter{

	@Override
	public void handle(Request request, Response response) throws Exception {
		boolean authenticated = false;
		String value = request.headers("Authorization");
		try{
			if(!Strings.isNullOrEmpty(value)){
				String jwtTocken = value.replace(BEARER, EMPTY_RESPONSE);
				if(AuthenticationService.isTrustJWT(jwtTocken)){
					String data = AuthenticationService.getJWTData(jwtTocken);
					@SuppressWarnings("unchecked")
					Map<String, Object> attrMap = new Gson().fromJson(data, Map.class);
					int user_id = ((Number)attrMap.get(USER_ID)).intValue();
					String time = (String)attrMap.get(EXPIRATION);
					request.raw().setAttribute(USER_ID, user_id);
					if(LocalDateTime.now().isBefore(LocalDateTime.parse(time))){ 
						authenticated = true;
					}
				}
			}
		}catch(Exception e){}
		if(!authenticated){
			halt(HTTP.UNAUTHORIZED, GO_AWAY);
		}
	}

}
