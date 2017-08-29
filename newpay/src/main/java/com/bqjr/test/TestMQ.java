package com.bqjr.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bqjr.model.EntityName;
import com.bqjr.mq.impl.RabbitMQReceiveServiceImpl;
import com.bqjr.mq.impl.RabbitMQSendServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:springmvc/applicationContext.xml"})
public class TestMQ {
	
	@Autowired
	private RabbitMQSendServiceImpl rabbitMQSendService;
	
	@Autowired
	private RabbitMQReceiveServiceImpl testReceiveService;
	
	@Autowired
	private AmqpTemplate sendAmqpTemplate;
	
	@Test
	public void testMqSend(){
		for(int i = 0;i<10;i++){
			sendAmqpTemplate.convertAndSend("TEST_QUEUE_KEY", "");
		}
		System.out.println("mq测试发送完成");
//		rabbitMQSendService.sendMsg("TEST_QUEUE_KEY", "hello world");
	}
	
	@Test
	public void testReceive(){
		testReceiveService.testFun1();
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
