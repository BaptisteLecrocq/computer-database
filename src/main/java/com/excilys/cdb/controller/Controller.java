package main.java.com.excilys.cdb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import main.java.com.excilys.cdb.model.*;
import main.java.com.excilys.cdb.service.CRUD;

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
	
	public boolean initPage(int type,int taille) {
		
		Page.count = service.countComputer();
		
		if(val.tailleValide(taille)) {
			if(type == 0) {
				page = computerFactory.getPage(0, taille);
			}
			else if(type == 1) {
				page = companyFactory.getPage(0, taille);
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public void nextPage() {
		page = page.nextPage();
	}
	
	public void previousPage() {
		page = page.previousPage();
	}
	
	public Computer getComputerById(int id) {
		return(service.getComputerById(id));
	}
	
	public boolean addComputer() {
		return(service.addComputer(computer));
	}

	public boolean updateComputer() {
		return(service.updateComputer(computer));
	}
	
	public boolean deleteComputer(int idComputer) {
		return(service.deleteComputer(idComputer));
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
		if(val.nameValide(name)) {
			this.name = name;
			return true;
		}
		else {
			return false;
		}
		
	}

	public boolean setStart(String start) {
		if(start.equals("n")) {
			this.introduced = null;
			return true;
		}
		else {
			try {
				this.introduced = LocalDate.parse(start,formatter);
				return true;
			} catch (Exception e) {
				return false;
			} 
		}
	}

	public boolean setEnd(String end) {
		if(end.equals("n")) {
			this.discontinued = null;
			return true;
		}
		else {
			try {
				LocalDate dateTest = LocalDate.parse(end,formatter);
				if(val.startBeforeEndValide(this.getStart(),dateTest)) {
					this.discontinued = dateTest;
					return true;
				}
				else {
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
