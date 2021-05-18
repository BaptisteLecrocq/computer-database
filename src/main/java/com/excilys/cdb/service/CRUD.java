package main.java.com.excilys.cdb.service;

import main.java.com.excilys.cdb.dao.*;
import main.java.com.excilys.cdb.mapper.Mapper;
import main.java.com.excilys.cdb.model.*;

import java.util.ArrayList;

public class CRUD {
	
	private Mapper map = Mapper.getInstance();
	
	//Singleton pattern	
	private static CRUD firstCrud = new CRUD();
	public static CRUD getInstance() {
		return(firstCrud);
	}
	
	public int countComputer() {
		return(map.countComputer());
	}
	
	public ArrayList<Computer> listComputer(){
		return(map.listComputer());
	}
	
	public ArrayList<Company> listCompany(){
		return(map.listCompany());
	}
	
	public boolean addComputer(Computer computer) {
		return(map.addComputer(computer));	
	}
	
	public boolean updateComputer(Computer replace) {		
		return(map.updateComputer(replace));	
	}
	
	public boolean deleteComputer(int id){
		return(map.deleteComputer(id));
	}	
	
	public Computer getComputerById(int id) {		
		return(map.getOneComputer(id));		
	}

	public ArrayList<Computer> pageComputerList(int start, int taille){
		return(map.getPageComputer(start,taille));
	}
	public ArrayList<Company> pageCompanyList(int start, int taille){
		return(map.getPageCompany(start,taille));
	}	
}
