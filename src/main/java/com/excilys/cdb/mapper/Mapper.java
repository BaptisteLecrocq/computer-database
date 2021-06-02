package com.excilys.cdb.mapper;

import java.sql.ResultSet;  

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.model.*;

public class Mapper {
	
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);
	
	private static Mapper firstMapper = new Mapper();
	public static Mapper getInstance() {
		return (firstMapper);
	}
	
	private Mapper() {};


 	public int countComputer( Optional<ResultSet> results ) {
		
		int i = 0;
		
		if(results.isPresent()) {
			try {
				results.get().next();
				i = results.get().getInt(1);
				
			} catch (SQLException e) {
				logger.error(e.toString());
			}
		}
		else {
			logger.error("No Computer Found");
		}
		
		return(i);
	}
	
	public int countCompany( Optional<ResultSet> results ) {

		int i = 0;
		
		if(results.isPresent()) {
			try {
				results.get().next();
				i = results.get().getInt(1);
				
			} catch (SQLException e) {
				logger.error(e.toString());
			}
		}
		else {
			logger.error("No Company found");
		}
		
		return(i);
	}
	
	public Optional<Computer> getOneComputer( Optional<ResultSet> results ) throws NotFoundException {
		
		ArrayList test =  map(Computer.class,results);
		Optional<Computer> computer = Optional.ofNullable((Computer)test.get(0));
		
		return(computer);
	}
	
	public ArrayList<Computer> mapComputerList( Optional<ResultSet> results ) throws NotFoundException {
		return(map(Computer.class,results));
	}
	
	public ArrayList<Company> mapCompanyList( Optional<ResultSet> results ) throws NotFoundException {
		return(map(Company.class,results));
	}
	
	public ArrayList map(Class type, Optional<ResultSet> optional) throws NotFoundException{		

		ArrayList list = new ArrayList();
		ResultSet results = optional.orElse(null);
		
		try {
			
			//isBeforeFirst = false => Empty ResultSet
			if(!optional.isPresent()||!results.isBeforeFirst()) {
				throw new NotFoundException("Empty ResultSet : the request didn't find anything in the database");
			}
			else {
				
			
			
				while(results.next()) {
					
					if(type==Computer.class) {
						Computer buffer = new Computer
								.ComputerBuilder(results.getString("computer.name"))
								.withId(results.getInt("computer.id"))
								.withStart(results.getObject("computer.introduced",LocalDate.class))
								.withEnd(results.getObject("computer.discontinued",LocalDate.class))
								.withManufacturer(results.getInt("computer.company_id"),results.getString("company.name"))
								.build();
						
						list.add(buffer);
					}
					else if(type==Company.class){
						Company buffer = new Company(results.getInt("company.id"), results.getString("company.name"));				
						list.add(buffer);
					}				
				}
			}
			
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		return(list);
	}

}
