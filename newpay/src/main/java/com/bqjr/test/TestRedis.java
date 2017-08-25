package com.bqjr.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springmvc/applicationContext.xml"})
public class TestRedis {
	
//	@Autowired
//	JedisCluster jedisCluster;
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	
	@Test
	public void test() {
//		try {
//		for(int i = 0 ; i < 100 ; i++) {
//			jedisCluster.set("key" + i, "value" + i);
//		}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		
		 redisTemplate.execute(new RedisCallback() {
	            public Long doInRedis(RedisConnection connection) throws DataAccessException {
	                connection.set("key2".getBytes(), "value3".getBytes());
	                return 1L;
	            }
	        });
		
	}
	

}
