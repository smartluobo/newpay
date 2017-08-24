package com.bqjr.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.JedisCluster;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springmvc/applicationContext.xml"})
public class TestRedis {
	
	@Autowired
	JedisCluster jedisCluster;
	
	@Test
	public void test() {
		try {
		for(int i = 0 ; i < 100 ; i++) {
			jedisCluster.set("key" + i, "value" + i);
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
