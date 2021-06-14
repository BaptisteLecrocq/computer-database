package com.excilys.cdb.validator;

import java.time.LocalDate; 
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.model.RequestParameter;
import com.excilys.cdb.service.CRUD;

@Component
public class ValidationDTO {
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private static final String ERR_NAME = "error.name";
	private static final String ERR_DATE_ORDER = "error.dateOrder";
	private static final String ERR_DATE_ABSENT = "error.absentIntroduction";
	private static final String ERR_FORMAT_INTRODUCED = "error.formatIntroduced";
	private static final String ERR_FORMAT_DISCONTINUED = "error.formatDiscontinued";
	private static final String ERR_FORMAT_COMPANY_ID = "error.formatCompany";
	private static final String ERR_COMPANY = "error.noCompany";
	
	
	@Autowired
	private CRUD service;	
	
	public ArrayList<String> validateComputerBean(ComputerBean cBean) {
		
		ArrayList<String> errors = new ArrayList<String>();
		
		if(validateName(cBean.getName())) { errors.add(ERR_NAME); }
		if(validateDateFormat(cBean.getIntroduced())) { errors.add(ERR_FORMAT_INTRODUCED);	}
		if(validateDateFormat(cBean.getDiscontinued())) { errors.add(ERR_FORMAT_DISCONTINUED);	}
		else {
			if(validateDiscontinuedButNoIntroduced(cBean.getIntroduced(),cBean.getDiscontinued())) { errors.add(ERR_DATE_ABSENT); }
			if(validateIntroducedBeforeDiscontinued(cBean.getIntroduced(),cBean.getDiscontinued())) { errors.add(ERR_DATE_ORDER); }
		}
		
		
		if(validateCompanyIdFormat(cBean.getCompany())) { errors.add(ERR_FORMAT_COMPANY_ID); } 
		else {
			if(validateCompanyExists(cBean.getCompany())) { errors.add(ERR_COMPANY); }
		}
		
		return(errors);
	}
	
	private boolean validateName(String name) {
		return(name == null || name.length() == 0 );
	}
	
	private boolean validateDateFormat(String date) {
		if (date.equals("n") || date.length() == 0 || date == null) {
			return false;		
		}
		else {
			try {
				LocalDate.parse(date, formatter);
				return false;
			}
			catch(Exception e) {
				e.printStackTrace();
				return true;
			}
		}		
	}
	
	private boolean validateDiscontinuedButNoIntroduced(String start, String end) {
		return( (start == null || start.length() == 0) && (end != null || end.length() > 0) );
	}
	
	private boolean validateIntroducedBeforeDiscontinued(String start, String end) {
			
			LocalDate introduced = null;
			LocalDate discontinued = null;
		
			try {
				introduced = LocalDate.parse(start, formatter);
				discontinued = LocalDate.parse(end, formatter);
			
			} catch(Exception e) {
				return true;
			}
			
			if(introduced == null || start.length() == 0 || discontinued == null || end.length() == 0 ) {
				return false;
				
			} else {
				return((discontinued.isBefore(introduced)));
			}
			
	}

	private boolean validateCompanyIdFormat(String id) {
		try {
			Integer.parseInt(id);
			return false;
			
		} catch(Exception e) {
			
			e.printStackTrace();
			return true;
		}
		
	}
	
	private boolean validateCompanyExists(String id) {
		
		int companyId = Integer.parseInt(id);
		int last = service.countCompany(new RequestParameter());
		return(companyId < 0 || companyId > last);
	}
}
