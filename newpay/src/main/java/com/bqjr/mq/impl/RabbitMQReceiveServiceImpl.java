package com.bqjr.mq.impl;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class RabbitMQReceiveServiceImpl implements MessageListener {

	@Override
	public void onMessage(Message message) {
		
		System.out.println(message);
		
	}
	
	public void testFun1(){
		int i = 0;
		while(i<10){
			i++;
			System.out.println("正在运行");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
		
	}

}
