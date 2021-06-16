package com.excilys.cdb.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.service.CompanyService;

@Component
public class ValidationCLI {
		
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static Logger logger = LoggerFactory.getLogger(ValidationCLI.class);
	
	private CompanyService service;
	
	public ValidationCLI(CompanyService service) {
		
		this.service = service;
		
	}
		
	private static final String nullName = "Name can't be null";
	private static final String wrongDateFormat = "Wrong Date Format";
	private static final String wrongPresence = "There can't be a Discontinuation date without an Introduction date";
	private static final String orderError = "Discontunation Date can't be higher than Introduction Date";
	private static final String invalidCompanyId = "CompanyId is too low";
	private static final String impossibleCompanyId = "CompanyId is too high";
	private static final String invalidSize = "Invalid Size";
	

	
	/*         Public Validation          */
	
	public ArrayList<String> valName(String name) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		if( name == null || name.length() == 0 ) {
			error.add(nullName);
		}
		
		return(error);
		
	}
	
	public ArrayList<String> valStart(String start) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		error.addAll(valDate(start));
		
		return(error);
	}
	
	public ArrayList<String> valEnd(String start, String end) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		error.addAll(valDate(end));
		error.addAll(valPresent(start, end));
		error.addAll(valStartBeforeEnd(start, end));
		
		return(error);
	}
	
	public ArrayList<String> valCompanyId(int id) {
		
		ArrayList<String> error = new ArrayList<String>();
		int last = service.getLastCompanyId();
		
		if( id < 0 ) {
			error.add(invalidCompanyId);
		}
		if( id > last ) {
			error.add(impossibleCompanyId);
		}
		
		return error;
		
	}
	
	public ArrayList<String> valTaille(int taille) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		if( taille < 0 ) {
			error.add(invalidSize);
		}
		
		return(error);
		
	}
	
	/*         Private Validation         */
	
 	private ArrayList<String> valDate(String date) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		if ( !(date.equals("n") || date.length() == 0 || date == null) ) {
			try {
				LocalDate.parse(date, formatter);
				error.add(wrongDateFormat);
			}
			catch(Exception e) {
				logger.debug(e.toString());
				e.printStackTrace();
			}
		}
		
		return(error);
	}
	
	private ArrayList<String> valPresent(String start, String end) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		if( (start.equals("n") || start.length() == 0 || start == null) && !(end.equals("n") || end.length() == 0 || end == null)) {
			error.add(wrongPresence);
		}
		
		return(error);
		
	}
	
	private ArrayList<String> valStartBeforeEnd(String start, String end) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		LocalDate introduced = null;
		LocalDate discontinued = null;
	
		try {
			introduced = LocalDate.parse(start, formatter);
			discontinued = LocalDate.parse(end, formatter);
		
		} catch(Exception e) {
			return error;
		}
		
		if(introduced == null || start.length() == 0 || discontinued == null || end.length() == 0 ) {
			return error;
			
		} else if ( discontinued.isBefore(introduced) ) {
			error.add(orderError);
		}
		
		return(error);
		
	}

}
