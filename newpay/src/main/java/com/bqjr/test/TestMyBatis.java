package com.bqjr.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bqjr.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springmvc/applicationContext.xml"})
public class TestMyBatis {
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testFun1(){
		List<Map<String,Object>> userMqs = userDao.getUserMq();
		for (Map<String, Object> map : userMqs) {
			System.out.println(map);
		}
	}

}
