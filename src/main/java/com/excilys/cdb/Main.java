package com.excilys.cdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.ui.CLI;

@Configuration
@ComponentScan( basePackages = { "com.excilys.cdb.controller", 
									"com.excilys.cdb.service", 
									"com.excilys.cdb.dao", 
									"com.excilys.cdb.mapper", 
									"com.excilys.cdb.dao",
									"com.excilys.cdb.servlets",
									"com.excilys.cdb.ui",
									"com.excilys.cdb.validator"} )
//@PropertySource( "chemin vers le fichier de config des propriétés" )
public class Main {

	private static CLI test;	
	
	public final static void main(String[] args) {
		
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.debug("Coucou");
		
		//@Bean
		
		
		ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
		test = context.getBean(CLI.class);
		test.init();
		

	}

}
