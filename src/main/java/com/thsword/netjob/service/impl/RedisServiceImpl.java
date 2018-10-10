package com.thsword.netjob.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thsword.netjob.dao.JedisFactory;
import com.thsword.netjob.service.RedisService;
import com.thsword.netjob.util.SerializeUtil;

import redis.clients.jedis.JedisCluster;

@Service("redisService")
public class RedisServiceImpl implements RedisService{
	@Autowired
	private JedisFactory jedisClusterFactory;
	
	private JedisCluster jedisCluster;
	
	@PostConstruct
	public void init() {
		jedisCluster = jedisClusterFactory.getObject();
	}
	
	@Override
	public String set(String key, String value){
		return jedisCluster.set(key, value);
	}

	@Override
	public String set(String key, Object object) {
		return jedisCluster.set(key.getBytes(), SerializeUtil.serialize(object));
	}
	
	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}
	
	@Override
	public Object getObject(String key) {
		return SerializeUtil.unserialize(jedisCluster.get(key.getBytes()));
	}

	@Override
	public Long del(String key) {
		return jedisCluster.del(key);
	}

	@Override
	public String set(String key, String value, int seconds) {
		return jedisCluster.setex(key, seconds, value);
	}
	
	@Override
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public Long pexpireAt(String key, long millisecondsTimestamp) {
		return jedisCluster.pexpireAt(key, millisecondsTimestamp);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}
}
