package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.CompanyBeanCLI;
import com.excilys.cdb.beans.ComputerBeanCLI;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

@Component
public class MapperCLI {
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static Logger logger = LoggerFactory.getLogger(MapperCLI.class);
	
	public Computer mapComputerCliDTOToModel( ComputerBeanCLI cBean ) {
		
		String start = cBean.getIntroduced();
		String end = cBean.getDiscontinued();
		
		LocalDate introduced = null;
		LocalDate discontinued = null;
		
		

		if( !("n".equals(start) || start == null) ){
			
			try {
				introduced = LocalDate.parse(start, formatter);
				
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			} 
		}
		
		if( !("n".equals(end) || end == null) ){
			
			try {
				discontinued = LocalDate.parse(end, formatter);
				
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			} 
		}
		
		
		Computer computer = new Computer
				.ComputerBuilder(cBean.getName())
				.withId(cBean.getId())
				.withStart(introduced)
				.withEnd(discontinued)
				.withManufacturer(cBean.getCompany_id(), null)
				.build();
		
		return(computer);		
	}
	
	public Company mapCompanyCliDTOToModel( CompanyBeanCLI cBean ) {
		
		Company company = new Company(cBean.getId(),cBean.getName());
		
		return(company);
		
	}
	
	

}
