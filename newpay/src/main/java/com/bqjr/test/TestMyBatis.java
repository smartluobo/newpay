package com.bqjr.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bqjr.dao.EntityNameMapper;
import com.bqjr.dao.UserDao;
import com.bqjr.model.EntityName;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springmvc/applicationContext.xml"})
public class TestMyBatis {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private EntityNameMapper mapper;
	
	@Test
	public void testFun1(){
		PageHelper.startPage(1,3);
		List<Map<String,Object>> userMqs = userDao.getUserMq();
//		PageHelper.
		Page<List<Map<String,Object>>> p = new Page<>();
		System.out.println(((Page) userMqs).getTotal()); 
		for (Map<String, Object> map : userMqs) {
			System.out.println(map);
		}
	}
	
	@Test
	public void testFun2(){
		//PageHelper.startPage(2, 3);
		List<EntityName> lists = mapper.selectByPrimaryKey("20170627000403");
		for (EntityName entityName : lists) {
			System.out.println(entityName);	
		}
//		long total = ((Page<EntityName>)lists).getTotal();
//		System.out.println(total);
	}

}
