package com.excilys.cdb.model;

import java.time.LocalDate;


public class Computer {
	
	//Auto increment
	private int id;
	//Can't be null
	private String name;
	//Can be null
	private LocalDate start;
	private LocalDate end;
	private Company manufacturer;
	
	private Computer(ComputerBuilder builder) {
		
		this.id = builder.id;
		this.name = builder.name;
		this.start = builder.start;
		this.end = builder.end;
		this.manufacturer = builder.manufacturer;
		
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

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public Company getManufacturer() {
		if(manufacturer==null) {
			return(new Company(0,null));
		}
		else {
			return manufacturer;
		}
	}
	
	public int getCompanyId() {
		return manufacturer.getId();
	}

	public void setManufacturer(Company manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public String toString() {
		String result = "";
		result += "Id : "+this.getId()+" : "+this.getName()+" | "+"Introduction Date : "+this.getStart()+" | "+"Discontuation Date : "+this.getEnd()+" | "+"Manufacturer : "+this.getManufacturer()+"\n";
		return(result);
	}
	
	public static class ComputerBuilder {
		
		private int id;
		private String name;
		private LocalDate start;
		private LocalDate end;
		private Company manufacturer;
		
		
		public ComputerBuilder(String name) {
			this.name = name;
		}
		
		
		public ComputerBuilder withId(int id) {
			this.id = id;
			return(this);
		}
		
		public ComputerBuilder withStart(LocalDate start) {
			this.start = start;
			return(this);
		}
		
		public ComputerBuilder withEnd(LocalDate end) {
			this.end = end;
			return(this);
		}
		
		public ComputerBuilder withManufacturer(int company_id, String company_name) {
			this.manufacturer = new Company(company_id, company_name);
			return(this);
		}
		
		public Computer build() {
			Computer computer = new Computer(this);
			return(computer);
		}	
	
	}
	
}
