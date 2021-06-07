package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.CompanyBeanDb;
import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.dao.ValidationDAO;
import com.excilys.cdb.exception.ValidationException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;


@Component
public class MapperDTOdb {
	
	private ValidationDAO valDao = new ValidationDAO();
	private static Logger logger = LoggerFactory.getLogger(MapperDTOdb.class);
	
	public ComputerBeanDb mapComputerModelToDTOdb( Computer computer ) {
		
		ComputerBeanDb cBean = new ComputerBeanDb();		
		
		cBean.setId(computer.getId());
		cBean.setName(computer.getName());
		
		Optional<LocalDate> introduced = Optional.ofNullable(computer.getStart());
		Optional<LocalDate> discontinued = Optional.ofNullable(computer.getEnd());
		
		if(introduced.isPresent()) {
			cBean.setIntroduced(introduced.toString());
		
		} else {
			cBean.setIntroduced(null);
		}
		
		if(discontinued.isPresent()) {
			cBean.setDiscontinued(discontinued.toString());
		
		} else {
			cBean.setDiscontinued(null);
		}
		
		cBean.setCompanyId(computer.getManufacturer().getId());
		
		try {
			valDao.validateComputerBeanDb(cBean);
			
		} catch (ValidationException e) {
			logger.debug(e.getMessage());
		}
		
		return(cBean);
	}
	
	public CompanyBeanDb mapCompanyModeltoDTOdb( Company company) {
		
		//valDao.validateCompany( company );
		CompanyBeanDb cBean = new CompanyBeanDb();
		
		cBean.setId(company.getId());
		cBean.setName(company.getName());
		
		return(cBean);
		
	}
	
}
