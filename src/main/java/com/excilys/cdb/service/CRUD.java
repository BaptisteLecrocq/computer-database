package com.excilys.cdb.service;

import com.excilys.cdb.dao.DAO; 
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.exception.TransactionException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.RequestParameter;



import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CRUD {
	
	@Autowired
	private DAO dao;
	
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
	
	public void deleteComputerList(String list) {
		dao.deleteComputerList(list);
	}
	
	public void deleteCompany(int id) throws TransactionException {
		dao.deleteCompany(id);
	}
	
	public Optional<Computer> getComputerById(int id) {		
		return(dao.findComputer(id));		
	}

	
	/*           Page Requests              */
	
	public ArrayList<Computer> pageComputer(Page page) {
		return(dao.pageComputer(page));
	}
	
	public ArrayList<Company> pageCompany(Page page) {
		return(dao.pageCompany(page));
	}
	
}
