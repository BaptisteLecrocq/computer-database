package com.excilys.cdb.service;

import com.excilys.cdb.dao.*;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.model.*;

import java.util.ArrayList;
import java.util.Optional;

public class CRUD {
	
	private DAO dao = DAO.getInstance();
	
	//Singleton pattern	
	private static CRUD firstCrud = new CRUD();
	public static CRUD getInstance() {
		return(firstCrud);
	}
	
	public int countComputer() {
		return(dao.countComputer());
	}
	
	public int countCompany() {
		return(dao.countCompany());
	}
	
	public ArrayList<Computer> listComputer() throws NotFoundException{
		return(dao.listComputer());
	}
	
	public ArrayList<Company> listCompany() throws NotFoundException{
		return(dao.listCompany());
	}
	
	public boolean addComputer(Computer computer) {
		return(dao.addComputer(computer));	
	}
	
	public boolean updateComputer(Computer replace) {		
		return(dao.updateComputer(replace));	
	}
	
	public boolean deleteComputer(int id){
		return(dao.deleteComputer(id));
	}	
	
	public Optional<Computer> getComputerById(int id) {		
		return(dao.findComputer(id));		
	}

	public ArrayList<Computer> pageComputerList(int start, int taille) {
		return(dao.getPageComputer(start,taille));
	}
	public ArrayList<Company> pageCompanyList(int start, int taille) {
		return(dao.getPageCompany(start,taille));
	}	
}
