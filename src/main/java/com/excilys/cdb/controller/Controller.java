package com.excilys.cdb.controller;

import java.time.LocalDate; 
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.beans.CompanyBean;
import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.beans.RequestParameterBean;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.*;
import com.excilys.cdb.service.CRUD;

public class Controller {
	
	private CRUD service = CRUD.getInstance();
	private Scanner sc;
	private final PageComputerFactory computerFactory = new PageComputerFactory();
	private final PageCompanyFactory companyFactory = new PageCompanyFactory();
	private Page page;
	private Validation val = new Validation();
	private ValidateDTO valDTO = new ValidateDTO();
	private MapperDTO mapDTO = MapperDTO.getInstance();
	
	private Computer computer;
	private int id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private int company_id;
	private String company_name;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static Logger logger = LoggerFactory.getLogger(Controller.class);

	
	public Controller() {
		sc = new Scanner(System.in);
	}
	
	/*              Page Management                */

	public Optional<String> initPage(Class<?> type, int taille,RequestParameterBean pBean) {
		
		return (this.initPage(type, taille, 0, pBean));	

	}
	
	public Optional<String> initPage(Class<?> type, int taille, int numberPage, RequestParameterBean pBean) {
		
		
		Optional<String> message = Optional.empty();
		RequestParameter parameters = mapDTO.mapParameters(pBean);
		
		if (val.tailleValide(taille)) {
			if (type == Computer.class) {				
				page = computerFactory.getPage(numberPage*taille, taille, numberPage);
				page.setElements(service.pageComputer(numberPage*taille, taille, parameters));
				Page.count = service.countComputer(parameters);
				
			} else if (type == Company.class) {
				page = companyFactory.getPage(numberPage*taille, taille, numberPage);
				page.setElements(service.pageCompany(numberPage*taille, taille, parameters));
				Page.count = service.countCompany(parameters);
			}
			
		} else {
			message = Optional.of("Invalid size");
		}
		
		return (message);		

	}
	
	public String nextPage() {
		
		page = page.nextPage();
			
		return ("Page suivante :");
	}
	
	public String previousPage() {
		
		page = page.previousPage();

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

	public boolean updateComputer() {
		return (service.updateComputer(computer));
	}
	
	public boolean deleteComputer(int idComputer) {
		return (service.deleteComputer(idComputer));
	}
	
	public void deleteList(String list) {
		String[] computers = list.split(",");
		int id = 0;
		
		for(String s:computers) {
			
			id = Integer.parseInt(s);
			deleteComputer(id);
			
		}
	}
	
	
	/*             CRUD Request towards/from Web UI          */
	
	public ArrayList<String> addComputerBean(ComputerBean cbean) {
		
		ArrayList<String> errors = valDTO.validateComputerBean(cbean);
		
		if(errors.isEmpty()) {
			initComputer();
			setComputer(mapDTO.mapDTOToComputer(cbean));
			addComputer();			
		}
		
		return(errors);
		
	}
	
	public ArrayList<String> editComputerBean(ComputerBean cbean) {
		
		ArrayList<String> errors = valDTO.validateComputerBean(cbean);
		
		if(errors.isEmpty()) {
			initComputer();
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
	
	
	

	/*         CLI Computer Management         */
	
 	public void initComputer() {
		this.id = 0;
		this.name = null;
		this.introduced = null;
		this.discontinued = null;
		this.company_id = 0;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public boolean setName(String name) {
		if (val.nameValide(name)) {
			this.name = name;
			return true;
		
		} else {
			return false;
		}
		
	}

	public boolean setStart(String start) {
		if (start.equals("n") || start == null) {
			this.introduced = null;
			return true;
		
		} else {
			try {
				this.introduced = LocalDate.parse(start, formatter);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
		}
	}

	public boolean setEnd(String end) {
		if (end.equals("n") || end == null ) {
			this.discontinued = null;
			return true;
		
		} else {
			try {
				LocalDate dateTest = LocalDate.parse(end, formatter);
				if (val.startBeforeEndValide(this.getStart(), dateTest)) {
					this.discontinued = dateTest;
					return true;
				
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			} 
		}
	}

	public void setCompanyId(int company_id) {
		this.company_id = company_id;
	}
	
	public void setCompanyName(String company_name) {
		this.company_name = company_name;
	}
	
 	public void buildComputer() {
		this.computer = new Computer
				.ComputerBuilder(name)
				.withId(id)
				.withStart(introduced)
				.withEnd(discontinued)
				.withManufacturer(company_id, company_name)
				.build();
	}
	
 	
 	/*          Utility            */
	
	public LocalDate getStart() {
		return introduced;
	}
	
	public void setComputer(Computer c) {
		this.computer = c;
	}

	public void close() {
		sc.close();
	}

}
