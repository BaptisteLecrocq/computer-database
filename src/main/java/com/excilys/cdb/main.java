package com.excilys.cdb;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.ui.CLI;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

public class main {

	//int test = 1;
	
	public final static void main(String[] args) {
		// assume SLF4J is bound to logback in the current environment
		//LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// print logback's internal status
		//StatusPrinter.print(lc);

		Logger logger = LoggerFactory.getLogger(main.class);
		logger.debug("Coucou");
		  
		CLI test = new CLI();
		
		

	}

}
