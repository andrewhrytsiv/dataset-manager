package com.filter;

import static com.util.HTTPHelper.BEARER;
import static com.util.HTTPHelper.EMPTY_RESPONSE;
import static com.util.HTTPHelper.EXPIRATION;
import static com.util.HTTPHelper.GO_AWAY;
import static com.util.HTTPHelper.UNAUTHORIZED_STATUS;
import static com.util.HTTPHelper.USER_ID;
import static spark.Spark.halt;

import java.util.Date;
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
					int time = ((Number)attrMap.get(EXPIRATION)).intValue();
					request.raw().setAttribute(USER_ID, user_id);
//					Date now = new Date();
//					if(now.before(new Date(time))){ time wrong value to convert
						authenticated = true;
//					}
				}
			}
		}catch(Exception e){}
		if(!authenticated){
			halt(UNAUTHORIZED_STATUS, GO_AWAY);
		}
	}

}
