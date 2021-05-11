package com.excilys.service;

import com.excilys.dao.*;
import com.excilys.model.*;

import java.util.ArrayList;
import java.util.Date;

public class CRUD {
	
	private ArrayList<Computer> computerList;
	private ArrayList<Company> companyList;
	private Connect connection;
	
	//Singleton pattern
	private static CRUD firstCrud = new CRUD();
	private static CRUD getFirst() {
		return(firstCrud);
	}
	
	public CRUD() {
		
		connection = new Connect();
		computerList = connection.listComputer();
		companyList = connection.listCompany();		
		
		}
	
	public ArrayList<Computer> listComputer(){
		return(firstCrud.getComputerList());
	}
	
	public ArrayList<Company> listCompany(){
		return(firstCrud.getCompanyList());
	}
	
	public boolean add(Computer computer) {
		
		try {
			firstCrud.connection.addComputer(computer);
			firstCrud.computerList.add(computer);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean update(Computer replace) {
		
		Computer computer = firstCrud.getComputerById(replace.getId());
		
		if(computer==null) {
			return false;
		}
		else {
			
			computer.setName(replace.getName());
			computer.setStart(replace.getStart());
			computer.setEnd(replace.getEnd());
			computer.setManufacturer(replace.getManufacturer());
			
			firstCrud.connection.updateComputer(replace.getId(), computer);
			firstCrud.computerList.set(replace.getId()-1, computer);
			
			return(true);
		}		
	}
	
	public boolean delete(int id){
		
		Computer computer = firstCrud.getComputerById(id);
		
		if(computer == null) {
			return false;
		}
		else {
			
			firstCrud.connection.deleteComputer(id);
			firstCrud.computerList.remove(computer);
			
			return true;
		}
	}
	
	
	public Computer getComputerByName(String name) {		
		
		for(Computer c : firstCrud.computerList) {
			if(c.getName().equals(name)) {
				return(c);
			}
		}		
		
		// Not found Exception ?		
		return(null);		
	}
	
	public Computer getComputerById(int id) {		
		
		for(Computer c : firstCrud.computerList) {
			if(c.getId() == id){
				return(c);
			}
		}		
		
		// Not found Exception ?		
		return(null);		
	}

	public ArrayList<Computer> getComputerList() {
		return computerList;
	}

	public void setComputerList(ArrayList<Computer> computerList) {
		this.computerList = computerList;
	}

	public ArrayList<Company> getCompanyList() {
		return companyList;
	}

	/*
	public void setCompanyList(ArrayList<Company> companyList) {
		this.companyList = companyList;
	}
	*/
	public void CRUDstop() {
		firstCrud.connection.stop();
	}
	
}
