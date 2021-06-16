package com.excilys.cdb.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.CompanyDAO;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.exception.TransactionException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.RequestParameter;

@Service
public class CompanyService {

private CompanyDAO dao;
	
	public CompanyService( CompanyDAO dao ) {
		
		this.dao = dao;
		
	}
	
	
	public int countCompany(RequestParameter parameters) {
		return(dao.countCompany(parameters));
	}
	
	public int getLastCompanyId() {
		return(dao.getLastCompanyId());
	}
	
	/*            CRUD Requests            */

	
	public ArrayList<Company> listCompany(RequestParameter parameters) throws NotFoundException{
		return(dao.listCompany(parameters));
	}
	
	public void addCompany(Company company) {
		dao.addCompany(company);
	}

	public void deleteCompany(int id) throws TransactionException {
		dao.deleteCompany(id);
	}
	
	
	/*           Page Requests              */
	
	
	public ArrayList<Company> pageCompany(Page page) {
		return(dao.pageCompany(page));
	}
	
	
}
