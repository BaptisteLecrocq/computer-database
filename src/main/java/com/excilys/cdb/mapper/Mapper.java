package com.excilys.cdb.mapper;

import java.sql.ResultSet; 
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.model.*;
import com.excilys.cdb.service.CRUD;

public class Mapper {
	
	DAO connection = DAO.getInstance();
	private final int computerType = 0;
	private final int companyType = 1;
	
	//Singleton pattern	
	private static Mapper firstMapper = new Mapper();
	public static Mapper getInstance() {
		return(firstMapper);
	}
	
	public int countComputer() {
		ResultSet results = connection.countComputer();
		int i = 0;
		
		try {
			results.next();
			i = results.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Nb of values in computer database : "+i);
		return(i);
	}
	
	public ArrayList<Computer> listComputer (){
		return(Map(computerType,connection.listComputer()));
	}
	
	public ArrayList<Company> listCompany(){
		return(Map(companyType,connection.listCompany()));
	}
	
	public boolean addComputer(Computer computer) {
		return(connection.addComputer(computer.getName(), computer.getStart(), computer.getEnd(), computer.getCompanyId()));
	}
	
	public Computer getOneComputer(int id) {
		
		Computer computer;
		ArrayList test =  Map(computerType,connection.findComputer(id));
		
		if(test.size()>0) {
			computer = (Computer)test.get(0);
		}
		else {
			computer = null;
		}
		
		return(computer);
	}
	
	public boolean updateComputer(Computer computer) {
		return(connection.updateComputer(computer.getId(), computer.getName(), computer.getStart(), computer.getEnd(), computer.getCompanyId()));
	}
	
	public boolean deleteComputer(int id) {
		return(connection.deleteComputer(id));
	}
	
	public ArrayList<Computer> getPageComputer(int start, int taille){
		return(Map(computerType,connection.getPageComputer(start,taille)));
	}
	
	public ArrayList<Company> getPageCompany(int start, int taille){
		return(Map(companyType,connection.getPageCompany(start,taille)));
	}
	
	public ArrayList Map(int type, ResultSet results){		

		ArrayList list = new ArrayList();		

		try {
			while(results.next()) {
				
				if(type==computerType) {
					Computer buffer = new Computer
							.ComputerBuilder(results.getString("computer.name"))
							.withId(results.getInt("computer.id"))
							.withStart(results.getObject("computer.introduced",LocalDate.class))
							.withEnd(results.getObject("computer.discontinued",LocalDate.class))
							.withManufacturer(results.getInt("computer.company_id"))
							.build();
					
					list.add(buffer);
				}
				else if(type==companyType){
					Company buffer = new Company(results.getInt("company.id"), results.getString("company.name"));				
					list.add(buffer);
				}				
			}
			connection.stop();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return(list);
	}

}
