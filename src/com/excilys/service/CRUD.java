package com.excilys.service;

import com.excilys.model.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class CRUD {
	
	private ArrayList<Computer> computerList;
	private ArrayList<Company> companyList;
	
	public CRUD() {
		computerList = new ArrayList<Computer>();
		companyList = new ArrayList<Company>();
		
		}
	
	public ArrayList<Computer> list(){
		return(computerList);
	}
	
	public void add(int id, String name, Date start, Date end, int company_id) {
		
		Computer computer = new Computer(id,name,start,end,company_id);
		computerList.add(computer);
		
	}
	
	public void update(String name, Date start, Date end, Company manufacturer) throws NullPointerException {
		
		Computer computer = this.getComputerByName(name);
		
		if(computer==null) {
			throw new NullPointerException();
		}
		else {		
			computer.setStart(start);
			computer.setEnd(end);
			computer.setManufacturer(manufacturer);
		}		
	}
	
	public void delete(String name) throws NullPointerException{
		
		Computer computer = this.getComputerByName(name);
		
		if(computer == null) {
			throw new NullPointerException();
		}
		else {
			computerList.remove(computer);
		}
	}
	
	
	public Computer getComputerByName(String name) {
		
		
		for(Computer c : computerList) {
			if(c.getName().equals(name)) {
				return(c);
			}
		}		
		
		// Not found Exception ?
		
		return(null);
		
	}
	
}
