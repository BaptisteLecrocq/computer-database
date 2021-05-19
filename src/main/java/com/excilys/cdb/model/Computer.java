package com.excilys.cdb.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDate;
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
	
	private static ArrayList<Integer> computerIdList;
	
	//Initialize computerIdList
	private static Computer initialize = new Computer();

	
	public Computer() {
			
		Connection con = null;
		String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db" ;

		try {
			con = DriverManager.getConnection(url,"admincdb","qwerty1234");
			
		
			String query = "SELECT * FROM computer;";
			
			int id;
			computerIdList = new ArrayList<Integer>();
			
			Statement stmt = con.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			while(results.next()) {
				
				id = results.getInt("computer.id");			
				computerIdList.add(id);		
			}		
			
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
		
		public ComputerBuilder withManufacturer(int company_id) {
			this.manufacturer = new Company(company_id);
			return(this);
		}
		
		public Computer build() {
			Computer computer = new Computer(this);
			return(computer);
		}	
	
	}
	
}
