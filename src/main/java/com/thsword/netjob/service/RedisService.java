package com.thsword.netjob.service;

public interface RedisService {
	
	String get(String key) ;

	Object getObject(String key) ;
	/**
	 * 删除已存在的键。不存在的 key 会被忽略。
	 * @param key
	 * @return 被删除 key 的数量。
	 */
	Long del(String key);
	
	String set(String key, String value);
	
	String set(String key, Object object);
	/**
	 * 设置key的value同时设置过期时间
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	String set(String key,String value,int seconds);
	
	/**
	 * 判断是否存在
	 * @param key
	 * @return
	 */
	Boolean exists(String key);
	
	/**
	 * 设置多少秒后过期
	 * @param key
	 * @param seconds
	 * @return 设置成功返回 1 。 当 key不存在或者不能为 key设置过期时间时返回 0
	 */
	Long expire(String key, int seconds);
	
	/**
	 * 指定过期时间
	 * @param key
	 * @param millisecondsTimestamp
	 * @return 设置成功返回 1 。 当 key不存在或者不能为 key设置过期时间时返回 0
	 */
	Long pexpireAt(String key, long millisecondsTimestamp);
	
	/**
	 * 对key的value值加1
	 * @param key
	 * @return
	 */
	Long incr(String key);
	
	/**
	 * 对key的value值加integer
	 * @param key
	 * @return
	 */
}
