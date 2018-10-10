package com.thsword.netjob.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thsword.netjob.service.RedisService;

/**
 * 
 * @author Administrator
 *
 */
@Component
public class RedisUtils{
	@Autowired
	private RedisService redisService;
	
	private static RedisUtils redisUtils;
	
    @PostConstruct  
    public void init() {  
    	redisUtils = this;  
    	redisUtils.redisService = this.redisService;  
  
    }  
	
	public static String set(String key, String value){
		return redisUtils.redisService.set(key, value);
	}
	
	public static String set(String key, Object object) {
		return redisUtils.redisService.set(key, object);
	}
	
	
	public static String get(String key) {
		System.out.println(redisUtils.redisService);
		return redisUtils.redisService.get(key);
	}
	
	
	public static Object getObject(String key) {
		return redisUtils.redisService.getObject(key);
	}

	
	public static Long del(String key) {
		return redisUtils.redisService.del(key);
	}

	
	public static String set(String key, String value, int seconds) {
		return redisUtils.redisService.set(key, value,seconds);
	}
	
	
	public static Boolean exists(String key) {
		return redisUtils.redisService.exists(key);
	}

	
	public static Long expire(String key, int seconds) {
		return redisUtils.redisService.expire(key, seconds);
	}

	
	public static Long pexpireAt(String key, long millisecondsTimestamp) {
		return redisUtils.redisService.pexpireAt(key, millisecondsTimestamp);
	}

	
	public static Long incr(String key) {
		return redisUtils.redisService.incr(key);
	}

}