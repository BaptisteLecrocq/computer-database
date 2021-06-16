package com.excilys.cdb.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.ComputerDAO;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.RequestParameter;

@Service
public class ComputerService {
	
private ComputerDAO dao;
	
	public ComputerService( ComputerDAO dao ) {
		
		this.dao = dao;
		
	}
	
	public int countComputer(RequestParameter parameters) {
		return(dao.countComputer(parameters));
	}
	
	public int getLastComputerId() {
		return(dao.getLastComputerId());
	}
	
	/*            CRUD Requests            */
	
	public ArrayList<Computer> listComputer(RequestParameter parameters) throws NotFoundException{
		return(dao.listComputer(parameters));
	}

	public boolean addComputer(Computer computer) {
		return(dao.addComputer(computer));
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
	
	public Computer getComputerById(int id) throws NotFoundException {		
		return(dao.findComputer(id));		
	}

	
	/*           Page Requests              */

	public ArrayList<Computer> pageComputer(Page page) {
		return(dao.pageComputer(page));
	}
	
	
	
	
}
