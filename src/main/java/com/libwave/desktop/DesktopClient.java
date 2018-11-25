package com.libwave.desktop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DesktopClient {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring-context.xml");
	
		ctx.start();
		
		
		ctx.stop();
	
	}
	

}
