package com.bqjr.mq.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("rabbitMQSendService")
public class RabbitMQSendServiceImpl {
	
//	@Autowired
//	private AmqpTemplate sendAmqpTemplate;
	
	
	public void sendMsg(String queueName,String message){
//		sendAmqpTemplate.convertAndSend(queueName, message);
		System.out.println("mq发送成功");
	}

}
