package com.excilys.cdb.controller;

import java.time.LocalDate; 
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;

import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.model.*;
import com.excilys.cdb.service.CRUD;

public class Controller {
	
	private CRUD service = CRUD.getInstance();
	private Scanner sc;
	private final PageComputerFactory computerFactory = new PageComputerFactory();
	private final PageCompanyFactory companyFactory = new PageCompanyFactory();
	private Page page;
	private Validation val;
	
	private Computer computer;
	private int id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private int company_id;
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	
	public Controller() {
		sc = new Scanner(System.in);
		val = new Validation();
	}
	
	public Optional<String> initPage(Class type, int taille) {
		
		Page.count = service.countComputer();
		Optional<String> message = Optional.empty();
		
		if (val.tailleValide(taille)) {
			try {
				
				if (type == Computer.class) {				
					page = computerFactory.getPage(0, taille);
					
				} else if (type == Company.class) {
					page = companyFactory.getPage(0, taille);
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
		if (start.equals("n")) {
			this.introduced = null;
			return true;
		
		} else {
			try {
				this.introduced = LocalDate.parse(start, formatter);
				return true;
			} catch (Exception e) {
				return false;
			} 
		}
	}

	public boolean setEnd(String end) {
		if (end.equals("n")) {
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
	
	public void close() {
		sc.close();
	}

}
