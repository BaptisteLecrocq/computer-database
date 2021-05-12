package com.excilys.model;

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
	
	//Singleton pattern
	private static Computer first = new Computer();
	private static Computer getFirst() {
		return(first);
	}
	
	public Computer() {
			
		Connection con = null;
		String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db" ;

		try {
			con = DriverManager.getConnection(url,"admincdb","qwerty1234");
			
		
			String query = "SELECT * FROM computer;";
			ResultSet results = null;
			
			int id;
			computerIdList = new ArrayList<Integer>();
			
			Statement stmt = con.createStatement();
			results = stmt.executeQuery(query);
			
			while(results.next()) {
				
				id = results.getInt("computer.id");			
				computerIdList.add(id);		
			}		
			
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Computer(int id, String name){
		
		this(id,name,null,null,0);
		
	}
	
	public Computer(int id, String name, LocalDate start, LocalDate end, int company_id) throws IllegalArgumentException{
		
		if(id<0) {
			this.id = computerIdList.get(computerIdList.size()-1) + 1;
		}
		else {
			this.id = id;
		}
		if(name==null) {
			throw new IllegalArgumentException();
		}
		else {
			this.name = name;
		}
		
		if(start!=null && end!=null) {
			if(end.isBefore(start)) {
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
