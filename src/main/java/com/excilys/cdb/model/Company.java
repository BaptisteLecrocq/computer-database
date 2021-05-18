package com.excilys.cdb.model;

import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.excilys.cdb.dao.*;

public class Company {
	
	private int id;
	private String name;

	private static ArrayList<Company> companyList;
	
	//Initialize companyList	
	private static Company initialize = new Company();	
	
	public Company() {
		
		Connection con = null;
		String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db" ;

		try {
			con = DriverManager.getConnection(url,"admincdb","qwerty1234");
			
		
			String query = "SELECT * FROM company;";
			ResultSet results = null;
			
			int id;
			String name;
			companyList = new ArrayList<Company>();
		
			Statement stmt = con.createStatement();
			results = stmt.executeQuery(query);
			
			while(results.next()) {
				
				id = results.getInt("company.id");
				name = results.getString("company.name");
				
				Company buffer = new Company(id, name);				
				companyList.add(buffer);
			}			
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Company(int id) {
		this.id = id;
		Company company = getCompanyById(id);
		if(company==null) {
			this.name = null;
		}
		else {
			this.name = company.getName();
		}				
	}
	
	public Company getCompanyById(int id) {

		ArrayList<Company> buffer = Company.getCompanyList();
		
		for(Company c : buffer) {
			if(c.getId()==id) {
				return(c);
			}
		}
		return(null);
	}
	
	public static ArrayList<Company> getCompanyList() {
		return companyList;
	}

	public static void setCompanyList(ArrayList<Company> companyList) {
		Company.companyList = companyList;
	}

	public Company(int id, String name) {
		this.id = id;
		this.name = name;
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
	
	public String toString() {
		String result = "";
		result +="{Id Company : "+this.id+" | Company Name : "+this.name+" }"+"\n";
		return(result);
	}

}
