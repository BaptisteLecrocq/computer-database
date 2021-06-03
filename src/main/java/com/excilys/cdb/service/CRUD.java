package com.excilys.cdb.service;

import com.excilys.cdb.dao.*;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.exception.TransactionException;
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
	
	public int countComputer(RequestParameter parameters) {
		return(dao.countComputer(parameters));
	}
	
	public int countCompany(RequestParameter parameters) {
		return(dao.countCompany(parameters));
	}
	
	public int getLastComputerId() {
		return(dao.getLastComputerId());
	}
	
	public int getLastCompanyId() {
		return(dao.getLastCompanyId());
	}
	
	/*            CRUD Requests            */

	public ArrayList<Computer> listComputer(RequestParameter parameters) throws NotFoundException{
		return(dao.listComputer(parameters));
	}
	
	public ArrayList<Company> listCompany(RequestParameter parameters) throws NotFoundException{
		return(dao.listCompany(parameters));
	}
	
	public boolean addComputer(Computer computer) {
		return(dao.addComputer(computer));	
	}
	
	public void addCompany(Company company) {
		dao.addCompany(company);
	}

	public boolean updateComputer(Computer replace) {
		return(dao.updateComputer(replace));	
	}
	
	public boolean deleteComputer(int id) {
		return(dao.deleteComputer(id));
	}	
	
	public void deleteCompany(int id) throws TransactionException {
		dao.deleteCompany(id);
	}
	
	public Optional<Computer> getComputerById(int id) {		
		return(dao.findComputer(id));		
	}

	
	/*           Page Requests              */
	
	public ArrayList<Computer> pageComputer(int start, int taille, RequestParameter parameters) {
		return(dao.pageComputer(start, taille, parameters));
	}
	
	public ArrayList<Company> pageCompany(int start, int taille, RequestParameter parameters) {
		return(dao.pageCompany(start, taille, parameters));
	}
	
}
