package com.excilys.cdb.mapper;

import java.sql.ResultSet;   

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.model.*;
import com.excilys.cdb.validator.ValidationDAO;

@Component
public class Mapper {
	
	@Autowired
	private static ValidationDAO val;
	
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);

 	public int countComputer( Optional<ResultSet> results ) {
		
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
