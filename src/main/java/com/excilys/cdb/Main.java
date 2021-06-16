package com.excilys.cdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.CLIConfig;
import com.excilys.cdb.config.SpringConfig;
import com.excilys.cdb.ui.CLI;


public class Main {

	private static CLI test;	
	
	public final static void main(String[] args) {
		
		
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.debug("Coucou");
		
		
		ApplicationContext context = new AnnotationConfigApplicationContext(CLIConfig.class);
		test = context.getBean(CLI.class);
		test.init();

	}



}
