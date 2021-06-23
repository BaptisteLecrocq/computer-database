package com.excilys.cdb.mapper;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.CompanyBeanDb;
import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.beans.RequestParameterBeanDb;
import com.excilys.cdb.beans.UserBean;
import com.excilys.cdb.exception.ValidationException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.RequestParameter;
import com.excilys.cdb.model.User;
import com.excilys.cdb.validator.ValidationDAO;


@Component
public class MapperDTOdb {
	
	private ValidationDAO valDao;
	
	private static Logger logger = LoggerFactory.getLogger(MapperDTOdb.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public MapperDTOdb ( ValidationDAO valDAO) {
		
		this.valDao = valDAO;
		
	}
	
	public Computer mapDAOBeanToComputer(ComputerBeanDb cBean) {
		
		//Validation from Database?
		
		Date start = cBean.getIntroduced();
		Date end = cBean.getDiscontinued();
		
		LocalDate introduced = null;
		LocalDate discontinued = null;
		
		if( start != null ){
			
			try {
				introduced = LocalDate.parse(start.toString().substring(0, 10), formatter);
				
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			} 
		}
		
		if( end != null ){
			
			try {
				discontinued = LocalDate.parse(end.toString().substring(0, 10), formatter);
				
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			} 
		}
		
		Computer buffer;
		
		if( cBean.getCompany() == null) {
			
			buffer = new Computer
					.ComputerBuilder(cBean.getName())
					.withId(cBean.getId())
					.withStart(introduced)
					.withEnd(discontinued)
					.build();
			
		} else {
			
			buffer = new Computer
					.ComputerBuilder(cBean.getName())
					.withId(cBean.getId())
					.withStart(introduced)
					.withEnd(discontinued)
					.withManufacturer(cBean.getCompany().getId(),cBean.getCompany().getName())
					.build();
			
		}
		
		
		
		return buffer;
		
	};

	public Company mapDAOBeanToCompany(CompanyBeanDb cBean) {
		
		Company company = new Company(cBean.getId(),cBean.getName());
		
		return(company);
		
	}
	
	public User mapDAOBeanToUser(UserBean uBean) {
		
		User user = new User
				.UserBuilder(uBean.getUsername())
				.withId(uBean.getId())
				.withPassword(uBean.getPassword())
				.withRole(uBean.getPassword())
				.withEnabled(uBean.isEnabled())
				.build();
		
		return(user);
	}
	
	public ComputerBeanDb mapComputerModelToDTOdb( Computer computer ) {
		
		ComputerBeanDb cBean = new ComputerBeanDb();		
		
		cBean.setId(computer.getId());
		cBean.setName(computer.getName());
		
		Optional<LocalDate> introduced = Optional.ofNullable(computer.getStart());
		Optional<LocalDate> discontinued = Optional.ofNullable(computer.getEnd());
		
		if(introduced.isPresent()) {
			cBean.setIntroduced(Date.valueOf(introduced.get()));
		
		} else {
			cBean.setIntroduced(null);
		}
		
		if(discontinued.isPresent()) {
			cBean.setDiscontinued(Date.valueOf(discontinued.get()));
		
		} else {
			cBean.setDiscontinued(null);
		}
		
		CompanyBeanDb companyBean = new CompanyBeanDb();
		
		companyBean.setId(computer.getManufacturer().getId());
		companyBean.setName(computer.getManufacturer().getName());
		
		cBean.setCompany(companyBean);
		
		try {
			valDao.validateComputerBeanDb(cBean);
			
		} catch (ValidationException e) {
			logger.debug(e.getMessage());
		}
		
		return(cBean);
	}
	
	public CompanyBeanDb mapCompanyModelToDTOdb( Company company ) {
		
		//valDao.validateCompany( company );
		CompanyBeanDb cBean = new CompanyBeanDb();
		
		cBean.setId(company.getId());
		cBean.setName(company.getName());
		
		return(cBean);
		
	}
	
	public UserBean mapUserModelToDTOdb(User user) {
		
		UserBean uBean = new UserBean();
		
		uBean.setId(user.getId());
		uBean.setUsername(user.getUsername());
		uBean.setPassword(user.getPassword());
		uBean.setRole(user.getRole());
		
		if(user.isEnabled()) {
			uBean.setEnabled(1);
		
		} else {
			uBean.setEnabled(0);			
		}
		
		return(uBean);
	}
	
	public RequestParameterBeanDb mapParametersToDTOdb( RequestParameter parameters) {
		
		//valDao.validateParameters ( parameters );
		RequestParameterBeanDb rBean = new RequestParameterBeanDb();
		
		rBean.setSearchTerm(parameters.getSearchTerm());
		rBean.setOrder(parameters.getOrder());
		rBean.setChoice(parameters.getChoice());
		
		return(rBean);
		
	}
	
}
