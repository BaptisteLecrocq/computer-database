package com.excilys.service;

import com.excilys.dao.*;
import com.excilys.model.*;

import java.util.ArrayList;

public class CRUD {
	
	private ArrayList<Computer> computerList;
	private ArrayList<Company> companyList;
	private Connect connection;
	
	//Singleton pattern
	
	private static CRUD firstCrud = new CRUD();
	public static CRUD getFirst() {
		return(firstCrud);
	}	
	
	public CRUD() {
		
		connection = new Connect();
		computerList = connection.listComputer();
		companyList = connection.listCompany();		
		
		}
	
	public ArrayList<Computer> listComputer(){
		return(getComputerList());
	}
	
	public ArrayList<Company> listCompany(){
		return(getCompanyList());
	}
	
	public boolean add(Computer computer) {
		
		try {
			connection.addComputer(computer);			
			computerList.add(computer);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean update(Computer replace) {
		
		int index = getComputerIndexById(replace.getId());
		
		if(index==0) {
			return false;
		}
		else {
			
			connection.updateComputer(replace.getId(), replace);
			computerList.set(index, replace);
			
			return(true);
		}		
	}
	
	public boolean delete(int id){
		
		int index = getComputerIndexById(id);
		
		if(index == 0) {
			return false;
		}
		else {
			
			connection.deleteComputer(id);
			computerList.remove(index);
			
			return true;
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
	
	public int getComputerIndexById(int id) {		
		
		for(int i=0;i<computerList.size();i++) {
			Computer c = computerList.get(i);
			if(c.getId() == id){
				return(i);
			}
		}		
		
		// Not found Exception ?		
		return(0);		
	}
	
	public Computer getComputerById(int id) {		
		
		for(Computer c:computerList) {
			if(c.getId() == id){
				return(c);
			}
		}		
		
		// Not found Exception ?		
		return(null);		
	}

	public ArrayList<Computer> pageComputerList(int start, int taille){
		return(connection.getPageComputer(start,taille));
	}
	public ArrayList<Company> pageCompanyList(int start, int taille){
		return(connection.getPageCompany(start,taille));
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
		connection.stop();
	}
	
}
