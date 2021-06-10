package com.excilys.cdb.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.excilys.cdb.beans.CompanyBean;
import com.excilys.cdb.beans.CompanyBeanCLI;
import com.excilys.cdb.beans.ComputerBean;
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
import com.excilys.cdb.service.CRUD;
import com.excilys.cdb.validator.ValidationDTO;

@Controller
public class ControllerCentral {
	
	@Autowired
	private CRUD service;
	
	private final PageComputerFactory computerFactory = new PageComputerFactory();
	private final PageCompanyFactory companyFactory = new PageCompanyFactory();
	private Page page;
	
	private ValidationDTO valDTO;
	private MapperDTO mapDTO;
	private MapperCLI mapCLI;
	
	private Computer computer;	
	private Company company;
	
	private static Logger logger = LoggerFactory.getLogger(Controller.class);
	
	public ControllerCentral (ValidationDTO valDTO, MapperDTO mapDTO, MapperCLI mapCLI) {
		
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
			Page.count = service.countComputer(parameters);
			
		} else if (type == PageCompany.class) {
			page = companyFactory.getPage(numberPage*taille, taille, numberPage, parameters);
			fillPage();
			Page.count = service.countCompany(parameters);
		}	

	}
	
	public void fillPage() {
		
		if( page.getClass() == null ) {
			logger.error("Page Class not attributed");
		
		} else {
			
			if( PageComputer.class.equals(page.getClass()) ) {
				page.setElements(service.pageComputer(page));
			
			} else if ( PageCompany.class.equals(page.getClass()) ){
				page.setElements(service.pageCompany(page));
			
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
			computerList = service.listComputer(parameters);
		} catch (NotFoundException e) {
			logger.info(e.toString());
			e.printStackTrace();
		}
		
		return(computerList);
	}
	
	public ArrayList<Company> listCompany(RequestParameter parameters){
		ArrayList<Company> companyList = new ArrayList<Company>();
		try {
			companyList = service.listCompany(parameters);
		} catch (NotFoundException e) {
			logger.toString();
			e.printStackTrace();
		}
		
		return(companyList);
	}
	
	public String getComputerById(int id) {
		
		String message = null;
		Optional<Computer> computer = service.getComputerById(id);
		
		if(computer.isPresent()) {
			message = computer.get().toString();
		}
		else {
			message = "Computer Not Found";
		}
		
		return(message);
	}
	
	public boolean addComputer() {
		return (service.addComputer(computer));
	}

	public void addCompany() {
		service.addCompany(company);
	}

	public boolean updateComputer() {
		return (service.updateComputer(computer));
	}
	
	public boolean deleteComputer(int idComputer) {
		return (service.deleteComputer(idComputer));
	}
	
	public ArrayList<String> deleteCompany(int idCompany) {
		
		ArrayList<String> error = new ArrayList<String>();
		
		try {
			service.deleteCompany(idCompany);
			
		} catch (TransactionException e) {
			logger.debug(e.getMessage());
			error.add(e.getMessage());
			e.printStackTrace();
		}
		
		return(error);
	}

 	public void deleteComputerList(String list) {
		service.deleteComputerList(list);
	}
	
	
	/*             CRUD Request towards/from Web UI          */
	
	public ArrayList<String> addComputerBean(ComputerBean cbean) {
		
		ArrayList<String> errors = valDTO.validateComputerBean(cbean);
		
		if(errors.isEmpty()) {
			
			this.computer = null;
			setComputer(mapDTO.mapDTOToComputer(cbean));
			addComputer();
			
		}
		
		return(errors);
		
	}

	public ArrayList<String> editComputerBean(ComputerBean cbean) {
		
		ArrayList<String> errors = valDTO.validateComputerBean(cbean);
		
		if(errors.isEmpty()) {
			
			this.computer = null;
			setComputer(mapDTO.mapDTOToComputer(cbean));
			updateComputer();	
		}
		
		return(errors);
		
	}

	public ArrayList<ComputerBean> listComputerBean (RequestParameter parameters) {
		
		ArrayList<Computer> computerList = listComputer(parameters);
		ArrayList<ComputerBean> searchResult = new ArrayList<ComputerBean>();
		
		for(Computer c:computerList) {
			searchResult.add(mapDTO.mapComputerToDTO(c));			
		}
		
		return(searchResult);
	}

	public ArrayList<CompanyBean> listCompanyBean(){
		
		ArrayList<CompanyBean> listBean = new ArrayList<CompanyBean>();
		ArrayList<Company> list = listCompany(null);
		
		
		for(Company c:list) {
			CompanyBean cBean = new CompanyBean();
			cBean.setId(c.getId());
			cBean.setName(c.getName());
			
			listBean.add(cBean);
		}
		return(listBean);		
		
	}

	public ArrayList<ComputerBean> pageComputerBean(){
		
		ArrayList<ComputerBean> pageComputerBean = new ArrayList<ComputerBean>();
		ArrayList<Computer> pageElements = (ArrayList<Computer>)(page.getElements());
		
		for(Computer c:pageElements) {				
			pageComputerBean.add(mapDTO.mapComputerToDTO(c));			
		}
		
		return(pageComputerBean);
		
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
