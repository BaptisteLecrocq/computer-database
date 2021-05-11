package com.excilys.model;

import java.util.ArrayList;
import java.util.Date;

public class Computer {
	
	//Auto increment
	private int id;
	//Can't be null
	private String name;
	//Can be null
	private Date start;
	private Date end;
	private Company manufacturer;
	
	//Singleton pattern
	private static ArrayList<Computer> computerList;
	private static Company first = new Computer();
	private static Company getFirst() {
		return(first);
	}
	
	public Computer(int id, String name) throws IllegalArgumentException{
		
		this.id = id;
		
		if(name==null) {
			throw new IllegalArgumentException();
		}
		else {
			this.name = name;
		}
	}
	
	public Computer(int id, String name, Date start, Date end, int company_id) throws IllegalArgumentException{
		
		if(name==null) {
			throw new IllegalArgumentException();
		}
		else {
			this.name = name;
		}
		
		if(start!=null && end!=null) {
			if(end.before(start)) {
				throw new IllegalArgumentException();
			}
		}
		this.start = start;
		this.end = end;		
		
		this.id = id;
		this.manufacturer = new Company(company_id);
	}
	
	public Computer(String name, Date start, Date end, int company_id) throws IllegalArgumentException{
		if(name==null) {
			throw new IllegalArgumentException();
		}
		else {
			this.name = name;
		}
		
		if(start!=null && end!=null) {
			if(end.before(start)) {
				throw new IllegalArgumentException();
			}
		}
		this.start = start;
		this.end = end;
		this.manufacturer = new Company(company_id);
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Company getManufacturer() {
		if(manufacturer==null) {
			return(new Company(-1,null));
		}
		else {
			return manufacturer;
		}
	}

	public void setManufacturer(Company manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public String toString() {
		String result = "";
		result += "Id : "+this.getId()+" : "+this.getName()+" | "+"Introduction Date : "+this.getStart()+" | "+"Discontuation Date : "+this.getEnd()+" | "+"Manufacturer : "+this.getManufacturer()+"\n";
		return(result);
	}
	
}
