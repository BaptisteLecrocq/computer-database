package com.excilys.cdb.controller;

import java.util.ArrayList; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.excilys.cdb.beans.CompanyBeanCLI;
import com.excilys.cdb.beans.ComputerBeanCLI;
import com.excilys.cdb.beans.RequestParameterBean;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.exception.TransactionException;
import com.excilys.cdb.mapper.MapperCLI;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.PageComputer;
import com.excilys.cdb.model.PageCompany;
import com.excilys.cdb.model.PageComputerFactory;
import com.excilys.cdb.model.PageCompanyFactory;
import com.excilys.cdb.model.RequestParameter;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.ValidationDTO;

@Controller
public class ControllerCentral {
	
	private ComputerService computerService;
	private CompanyService companyService;
	
	private final PageComputerFactory computerFactory = new PageComputerFactory();
	private final PageCompanyFactory companyFactory = new PageCompanyFactory();
	private Page page;
	
	private ValidationDTO valDTO;
	private MapperDTO mapDTO;
	private MapperCLI mapCLI;
	
	private Computer computer;	
	private Company company;
	
	private static Logger logger = LoggerFactory.getLogger(Controller.class);
	
	public ControllerCentral (ComputerService computerService, CompanyService companyService, ValidationDTO valDTO, MapperDTO mapDTO, MapperCLI mapCLI) {
		
		this.computerService = computerService;
		this.companyService = companyService;
		
		this.valDTO = valDTO;
		this.mapDTO = mapDTO;
		this.mapCLI = mapCLI;
		
	}

	
	/*              Page Management                */

	public void initPage(Class<?> type, int taille,RequestParameterBean pBean) {
		
		this.initPage(type, taille, 0, pBean);	

	}
	
	public void initPage(Class<?> type, int taille, int numberPage, RequestParameterBean pBean) {
		
		
		RequestParameter parameters = mapDTO.mapParameters(pBean);		

		if (type == PageComputer.class) {				
			page = computerFactory.getPage(numberPage*taille, taille, numberPage, parameters);
			fillPage();
			Page.count = computerService.countComputer(parameters);
			
		} else if (type == PageCompany.class) {
			page = companyFactory.getPage(numberPage*taille, taille, numberPage, parameters);
			fillPage();
			Page.count = companyService.countCompany(parameters);
		}	

	}
	
	public void fillPage() {
		
		if( page.getClass() == null ) {
			logger.error("Page Class not attributed");
		
		} else {
			
			if( PageComputer.class.equals(page.getClass()) ) {
				page.setElements(computerService.pageComputer(page));
			
			} else if ( PageCompany.class.equals(page.getClass()) ){
				page.setElements(companyService.pageCompany(page));
			
			} else {
				System.out.println(page.getClass());
			}
			
		}
		
		
	}
	
	public String nextPage() {
		
		page = page.nextPage();
		fillPage();
			
		return ("Page suivante :");
	}
	
	public String previousPage() {
		
		page = page.previousPage();
		fillPage();

		return ("Page précédente :");
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page p) {
		this.page = p;
	}
	
	
	/*            CRUD Requests towards Database             */
	
	

	public ArrayList<Computer> listComputer(RequestParameter parameters){
		ArrayList<Computer> computerList = new ArrayList<Computer>();
		try {
			computerList = computerService.listComputer(parameters);
		} catch (NotFoundException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
		
		return(computerList);
	}
	
	public ArrayList<Company> listCompany(RequestParameter parameters){
		ArrayList<Company> companyList = new ArrayList<Company>();
		try {
			companyList = companyService.listCompany(parameters);
		} catch (NotFoundException e) {
			logger.toString();
			e.printStackTrace();
		}
		
		return(companyList);
	}
	
	public String getComputerById(int id) {
		
		String message = null;
		Computer computer;
		
		try {
			
			computer = computerService.getComputerById(id);
			message = computer.toString();
			
		} catch (NotFoundException e) {
			
			message = "Computer Not Found";
			
		}
		
		return(message);
	}
	
	public boolean addComputer() {
		return (computerService.addComputer(computer));
	}

	public void addCompany() {
		companyService.addCompany(company);
	}

	public boolean updateComputer() {
		return (computerService.updateComputer(computer));
	}
	
	public boolean deleteComputer(int idComputer) {
		return (computerService.deleteComputer(idComputer));
	}
	
	public ArrayList<String> deleteCompany(int idCompany) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		try {
			
			companyService.deleteCompany(idCompany);
		
		} catch (TransactionException e) {
			
			logger.info(e.getMessage());
			error.add(e.getMessage());
			
		}
		
		
		return(error);
	}


	/*          Utility            */
	
 	public void setComputerCLI( ComputerBeanCLI cBean ) {
 		this.computer = mapCLI.mapComputerCliDTOToModel(cBean);
 	}

 	public void setCompanyCLI( CompanyBeanCLI cBean ) {
 		this.company = mapCLI.mapCompanyCliDTOToModel(cBean);
 	}

	public void setComputer(Computer c) {
		this.computer = c;
	}


}
