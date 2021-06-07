package com.excilys.cdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.ui.CLI;


public class Main {

	private static CLI test;	
	
	public final static void main(String[] args) {
		// assume SLF4J is bound to logback in the current environment
		//LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// print logback's internal status
		//StatusPrinter.print(lc);

		
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.debug("Coucou");
		
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		test = context.getBean(CLI.class);
		test.init();
		

	}

}
