package com.service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.UserDAO;
import com.entity.User;
import static com.util.AppConstants.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class AuthenticationService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
	public static int SYSTEM_USER_ID = 1; 

	@Autowired
	private UserDAO userDAO;
	
	private static final Key JWT_KEY = MacProvider.generateKey();
	
	private LoadingCache<Integer, User> cachedUsers = CacheBuilder.newBuilder().maximumSize(50)
			.expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<Integer, User>() {
				public User load(Integer userId) {
					return userDAO.find(userId);
				}
			});
	
	public User getUser(Integer id) throws ExecutionException{
		return cachedUsers.get(id);
	}
	
	public User getUser(String email){
		try{
			return userDAO.find(email);
		}catch(Exception ex){
			LOGGER.error(ex.getMessage(),ex);
		}
		return null;
	}
	
	public boolean registrer(User user){
		try{
			userDAO.insert(user);
		}catch(Exception ex){
			LOGGER.error(ex.getMessage(),ex);
			return false;
		}
		return true;
	}
	
	public boolean validateTocken(String jwtTocken) {
		try {
			if (jwtTocken != null && AuthenticationService.isTrustJWT(jwtTocken)) {
				String data = AuthenticationService.getJWTData(jwtTocken);
				@SuppressWarnings("unchecked")
				Map<String, Object> attrMap = new Gson().fromJson(data, Map.class);
				String time = (String) attrMap.get(EXPIRATION);
				if (LocalDateTime.now().isBefore(LocalDateTime.parse(time))) {
					return true;
				}
			}
		} catch (Exception ex) {}
		return false;
	}
	
	public String generateJWT(int userId){
		String exp = LocalDateTime.now().plusHours(6).toString();
		JsonObject tokenData = new JsonObject();
		tokenData.addProperty("user_id", userId);
		tokenData.addProperty("exp", exp);
		return createJWT(tokenData.toString());
	}
	
	private String createJWT(String data){
		return Jwts.builder().setSubject(data).signWith(SignatureAlgorithm.HS512, JWT_KEY).compact();
	}
	
	public static String getJWTData(String jwt){
		String data = null;
		try{
			Jws<Claims> claims = Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(jwt);
			data = claims.getBody().getSubject();
		}catch (SignatureException e) {}
		return data;
	} 
	
	public static boolean isTrustJWT(String jwt) {
		try {
			Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(jwt);
		} catch (SignatureException e) {
			return false;
		}
		return true;
	}
}
