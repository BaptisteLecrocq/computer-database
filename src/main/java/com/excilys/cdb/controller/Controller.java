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
	private Validation val;
	private ValidateDTO valDTO;
	private MapperDTO mapDTO;
	
	private Computer computer;
	private int id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private int company_id;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static Logger logger = LoggerFactory.getLogger(Controller.class);

	
	public Controller() {
		sc = new Scanner(System.in);
		val = new Validation();
		valDTO = new ValidateDTO();
		mapDTO = new MapperDTO();
	}
	
	public Optional<String> initPage(Class type, int taille) {
		
		return (this.initPage(type, taille, 0));		

	}
	
	public Optional<String> initPage(Class type, int taille, int numberPage) {
		
		Page.count = service.countComputer();
		Optional<String> message = Optional.empty();
		
		if (val.tailleValide(taille)) {
			try {
				
				if (type == Computer.class) {				
					page = computerFactory.getPage(numberPage*taille, taille, numberPage);
					
				} else if (type == Company.class) {
					page = companyFactory.getPage(numberPage*taille, taille, numberPage);
				}
				
			} catch (NotFoundException e) {
				message = Optional.of(e.toString());
			}
			
		} else {
			message = Optional.of("Invalid size");
		}
		
		return (message);		

	}
	
	public String nextPage() {
		String message;
		
		try {
			page = page.nextPage();
			message = "Page suivante :";
			
		} catch (NotFoundException e) {
			message = e.toString();
		}
		return (message);
	}
	
	public String previousPage() {
		String message;
		
		try {
			page = page.previousPage();
			message = "Page précédente :";
			
		} catch (NotFoundException e) {
			message = e.toString();
		}
		return (message);
	}
	
	
	
	public ArrayList<Computer> listComputer(){
		ArrayList<Computer> computerList = new ArrayList<Computer>();
		try {
			computerList = service.listComputer();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		
		return(computerList);
	}
	
	public ArrayList<Company> listCompany(){
		ArrayList<Company> companyList = new ArrayList<Company>();
		try {
			companyList = service.listCompany();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		
		return(companyList);
	}
	
	public String getComputerById(int id) {
		try {
			return (service.getComputerById(id).toString());
		} catch (NotFoundException e) {
			return (e.toString());
		}
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
	
	
	public ArrayList<String> addComputerBean(ComputerBean cbean) {
		
		ArrayList<String> errors = valDTO.validateComputerBean(cbean);
		
		if(errors.isEmpty()) {
			initComputer();
			setComputer(mapDTO.mapDTOToComputer(cbean));
			addComputer();			
		}
		
		return(errors);
		
	}

	public ArrayList<CompanyBean> listCompanyBean(){
		
		ArrayList<CompanyBean> list = new ArrayList<CompanyBean>();
		
		for(Company c:listCompany()) {
			CompanyBean cBean = new CompanyBean();
			cBean.setId(c.getId());
			cBean.setName(c.getName());
			
			list.add(cBean);
		}
		return(list);		
		
	}

	public ArrayList<ComputerBean> pageComputer(){
		
		ArrayList<ComputerBean> pageComputer = new ArrayList<ComputerBean>();
		
		Optional<LocalDate> introducedBuffer = Optional.empty();
		Optional<LocalDate> discontinuedBuffer = Optional.empty();
		
		for(Computer c:((ArrayList<Computer>)(page.getElements()))) {
			
			ComputerBean cBean = new ComputerBean();
			cBean.setName(c.getName());
			cBean.setCompany(c.getManufacturer().getName());
			
			introducedBuffer = Optional.ofNullable(c.getStart());
			if(introducedBuffer.isPresent()) {
				cBean.setIntroduced(introducedBuffer.get().toString());
			}
			
			discontinuedBuffer = Optional.ofNullable(c.getEnd());
			if(discontinuedBuffer.isPresent()) {
				cBean.setDiscontinued(discontinuedBuffer.get().toString());
			}
			
			
			pageComputer.add(cBean);
			
		}
		
		return(pageComputer);
		
	}
	
	

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

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	
	public void buildComputer() {
		this.computer = new Computer
				.ComputerBuilder(name)
				.withId(id)
				.withStart(introduced)
				.withEnd(discontinued)
				.withManufacturer(company_id)
				.build();
	}
	
	
	public LocalDate getStart() {
		return introduced;
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page p) {
		this.page = p;
	}
	
	public void setComputer(Computer c) {
		this.computer = c;
	}

	public void close() {
		sc.close();
	}

}
