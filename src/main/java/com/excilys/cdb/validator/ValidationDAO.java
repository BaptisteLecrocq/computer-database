package com.excilys.cdb.validator;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;   
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.exception.FormatException;
import com.excilys.cdb.exception.IdValidationException;
import com.excilys.cdb.exception.NameValidationException;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.exception.ValidationException;

@Component
public class ValidationDAO {
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private static final String ERR_DATE_ORDER = "Discontinuation date has to be later than Introduction date";
	private static final String ERR_DATE_ABSENT = "Can't have a discontinuation date without an introduction one";
	private static final String ERR_COMPANY = "Company id does not match any Company";
	
	private static final String NO_RESULT = "No Resultset found";
	private static final String EMPTY_RESULT = "Empty Resultset";
	
	
	/*           Public validators           */
	
	public void validateComputerBeanDb(ComputerBeanDb cBean) throws ValidationException {
		
		if(validateId(cBean.getId())) { throw new IdValidationException(); }
		if(validateName(cBean.getName())) { throw new NameValidationException(); }
		if(validateDateFormat(cBean.getIntroduced())) { throw new FormatException("Introduction date"); }
		if(validateDateFormat(cBean.getDiscontinued())) { throw new FormatException("Discontinuation date"); }
		else {
			if(validateDiscontinuedButNoIntroduced(cBean.getIntroduced(),cBean.getDiscontinued())) { throw new ValidationException(ERR_DATE_ABSENT); }
			if(validateIntroducedBeforeDiscontinued(cBean.getIntroduced(),cBean.getDiscontinued())) { throw new ValidationException(ERR_DATE_ORDER); }
		}

		if(validateCompanyExists(cBean.getCompany().getId())) { throw new ValidationException(ERR_COMPANY); }
		
	}
	
	public void validateFound(Optional<ResultSet> results) throws NotFoundException {
		
		
		if(results.isPresent()) {
			try {
				results.get().next();
			
			} catch( SQLException e ) {
				throw( new NotFoundException(EMPTY_RESULT, e));
			}
		
		} else {
			throw( new NotFoundException(NO_RESULT));
		}
		
	}
	
	
	/*             Private methods             */

	private boolean validateId(int id) { 
		return(id < 0);
	}
	
	private boolean validateName(String name) {
		return(name == null || name.length() == 0 );
	}
	
	private boolean validateDateFormat(Date date) {
		if ( date == null ) {
			return false;	
		}
		else {
			try {
				LocalDate.parse(date.toString(), formatter);
				return false;
			}
			catch(Exception e) {
				return true;
			}
		}		
	}
	
	private boolean validateDiscontinuedButNoIntroduced(Date start, Date end) {
		return(start == null && end != null );
	}
	
	private boolean validateIntroducedBeforeDiscontinued(Date start, Date end) {
			
			//Date start = Optional.ofNullable(start);
			//Date end = Optional.ofNullable(end);
		
			LocalDate introduced;
			LocalDate discontinued;
			
			if(start != null) {
				introduced = LocalDate.parse(start.toString().substring(0, 10), formatter);
			
			} else {
				introduced = null;
			}
			
			if(end != null) {
				discontinued = LocalDate.parse(end.toString().substring(0, 10), formatter);
			
			} else {
				discontinued = null;
			}
			
			
			if(introduced == null || start == null || discontinued == null || end == null ) {
				return false;
				
			} else {
				return((discontinued.isBefore(introduced)));
			}
			
	}
	
	private boolean validateCompanyExists(int id) {

		//int last = service.countCompany();
		int last = 43;
		return(id < 0 || id > last);
	}

}
