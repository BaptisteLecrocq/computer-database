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
	
	DAO connection = DAO.getInstance();
	
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);
	
	//Singleton pattern	
	private static Mapper firstMapper = new Mapper();
	public static Mapper getInstance() {
		return(firstMapper);
	}
	
	private Mapper(){}	

	public int countComputer() {
		Optional<ResultSet> results = connection.countComputer();
		int i = 0;
		
		if(results.isPresent()) {
			try {
				results.get().next();
				i = results.get().getInt(1);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			System.out.println("Nb of values in computer database : "+i);
		}
		else {
			//Exception
		}
		
		return(i);
	}
	
	public ArrayList<Computer> listComputer () throws NotFoundException{
		return(map(Computer.class,connection.listComputer()));
	}
	
	public ArrayList<Company> listCompany() throws NotFoundException{
		return(map(Company.class,connection.listCompany()));
	}
	
	public boolean addComputer(Computer computer) {
		return(connection.addComputer(computer.getName(), Optional.ofNullable(computer.getStart()), Optional.ofNullable(computer.getEnd()), computer.getCompanyId()));
	}
	
	public Computer getOneComputer(int id) throws NotFoundException {
		
		Computer computer;
		ArrayList test =  map(Computer.class,connection.findComputer(id));
		
		if(test.size()>0) {
			computer = (Computer)test.get(0);
		}
		else {
			computer = null;
		}
		
		return(computer);
	}
	
	public boolean updateComputer(Computer computer) {
		return(connection.updateComputer(computer.getId(), computer.getName(), Optional.ofNullable(computer.getStart()), Optional.ofNullable(computer.getEnd()), computer.getCompanyId()));
	}
	
	public boolean deleteComputer(int id) {
		return(connection.deleteComputer(id));
	}
	
	public ArrayList<Computer> getPageComputer(int start, int taille) throws NotFoundException{
		return(map(Computer.class,connection.getPageComputer(start,taille)));
	}
	
	public ArrayList<Company> getPageCompany(int start, int taille) throws NotFoundException{
		return(map(Company.class,connection.getPageCompany(start,taille)));
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
								.withManufacturer(results.getInt("computer.company_id"))
								.build();
						
						list.add(buffer);
					}
					else if(type==Company.class){
						Company buffer = new Company(results.getInt("company.id"), results.getString("company.name"));				
						list.add(buffer);
					}				
				}
			}
			
			connection.stop();
			
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		return(list);
	}

}
