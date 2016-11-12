package com.service;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.UserDAO;
import com.entity.User;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
	
	public String generateJWT(int userId){
		long exp = getExpirationDate();
		JsonObject tokenData = new JsonObject();
		tokenData.addProperty("user_id", userId);
		tokenData.addProperty("exp", exp);
		return createJWT(tokenData.toString());
	}
	
	private String createJWT(String data){
		return Jwts.builder().setSubject(data).signWith(SignatureAlgorithm.HS512, JWT_KEY).compact();
	}
	
	private long getExpirationDate(){
		ZonedDateTime zdateTime = ZonedDateTime.now();
		zdateTime = zdateTime.plusHours(12);
		return zdateTime.toEpochSecond();
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
