package com.excilys.cdb.mapper;

import java.sql.ResultSet;   

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.CompanyBeanDb;
import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.model.*;
import com.excilys.cdb.validator.ValidationDAO;

@Component
public class Mapper {
	
	@Autowired
	private static ValidationDAO val;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);

 	public int countComputer( Optional<ResultSet> results ) {
		
		int i = 0;
		
		try {
			//val.validateFound(results);
			results.get().next();
			i = results.get().getInt(1);
		/*
		} catch (NotFoundException e){
			logger.info(e.getMessage());
			e.printStackTrace();
			*/
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		return(i);
	}
	
	public int countCompany( Optional<ResultSet> results ) {

		int i = 0;
		
		try {
			val.validateFound(results);
			results.get().next();
			i = results.get().getInt(1);
		
		} catch (NotFoundException e){
			logger.info(e.getMessage());
			e.printStackTrace();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return(i);
	}
	
	public Computer mapDAOBeanToComputer(ComputerBeanDb cBean) {
		
		//Validation from Database?
		
		String start = cBean.getIntroduced();
		String end = cBean.getDiscontinued();
		
		LocalDate introduced = null;
		LocalDate discontinued = null;
		
		if( !("".equals(start) || start == null) ){
			
			try {
				start = start.substring(0, 10);
				introduced = LocalDate.parse(start, formatter);
				
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			} 
		}
		
		if( !("".equals(end) || end == null) ){
			
			try {
				end = end.substring(0, 10);
				discontinued = LocalDate.parse(end, formatter);
				
			} catch (Exception e) {
				logger.error(e.toString());
				e.printStackTrace();
			} 
		}
		
		Computer buffer = new Computer
				.ComputerBuilder(cBean.getName())
				.withId(cBean.getId())
				.withStart(introduced)
				.withEnd(discontinued)
				.withManufacturer(cBean.getCompanyId(),cBean.getCompanyName())
				.build();
		
		return buffer;
		
	};

	public Company mapDAOBeanToCompany(CompanyBeanDb cBean) {
		
		Company company = new Company(cBean.getId(),cBean.getName());
		
		return(company);
		
	}
	
	
	public Optional<Computer> getOneComputer( Optional<ResultSet> results ) throws NotFoundException {
		
		ArrayList<Computer> test =  mapComputer(results);
		Optional<Computer> computer = Optional.ofNullable((Computer)test.get(0));
		
		return(computer);
	}
	
	public ArrayList<Computer> mapComputerList( Optional<ResultSet> results ) throws NotFoundException {
		return(mapComputer(results));
	}
	
	public ArrayList<Company> mapCompanyList( Optional<ResultSet> results ) throws NotFoundException {
		return(mapCompany(results));
	}
	
	public ArrayList<Computer> mapComputer(Optional<ResultSet> optional) throws NotFoundException {
		
		ArrayList<Computer> list = new ArrayList<Computer>();
		ResultSet results = optional.orElse(null);
		
		try {
			
			//isBeforeFirst = false => Empty ResultSet
			if(!optional.isPresent()||!results.isBeforeFirst()) {
				throw new NotFoundException("Empty ResultSet : the request didn't find anything in the database");
			}
			else {
				
				while(results.next()) {
					
					Computer buffer = new Computer
							.ComputerBuilder(results.getString("computer.name"))
							.withId(results.getInt("computer.id"))
							.withStart(results.getObject("computer.introduced",LocalDate.class))
							.withEnd(results.getObject("computer.discontinued",LocalDate.class))
							.withManufacturer(results.getInt("computer.company_id"),results.getString("company.name"))
							.build();
					
					list.add(buffer);
					}
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		return(list);		
	}
	
	public ArrayList<Company> mapCompany(Optional<ResultSet> optional) throws NotFoundException {
		
		ArrayList<Company> list = new ArrayList<Company>();
		ResultSet results = optional.orElse(null);
		
		try {
			
			//isBeforeFirst = false => Empty ResultSet
			if(!optional.isPresent()||!results.isBeforeFirst()) {
				throw new NotFoundException("Empty ResultSet : the request didn't find anything in the database");
			}
			else {
				
				while(results.next()) {
					
					Company buffer = new Company(results.getInt("company.id"), results.getString("company.name"));				
					list.add(buffer);
					}
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		return(list);
		
	}
}
