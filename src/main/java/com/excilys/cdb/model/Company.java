package com.excilys.cdb.model;

import java.sql.Connection;   
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.DAO;

public class Company {
	
	private int id;
	private String name;

	private static ArrayList<Company> companyList;
	private static Logger logger = LoggerFactory.getLogger(DAO.class);
	
	//Initialize companyList
	private static Company initialize = new Company();
	
	
	public Company() {
		
		Optional<Connection> con = Optional.empty();
		String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db";

		try {
			con = Optional.ofNullable(DriverManager.getConnection(url, "admincdb", "qwerty1234"));
			
		
			String query = "SELECT * FROM company;";
			
			int id;
			String name;
			companyList = new ArrayList<Company>();
		
			Statement stmt = con.get().createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			while (results.next()) {
				
				id = results.getInt("company.id");
				name = results.getString("company.name");
				
				Company buffer = new Company(id, name);				
				companyList.add(buffer);
			}			
			con.get().close();
			
		} catch (SQLException e) {
			logger.error(e.toString());
		}
	}
	
	public Company(int id) {
		this.id = id;
		Optional<Company> company = getCompanyById(id);
		if (!company.isPresent()) {
			this.name = null;
		
		} else {
			this.name = company.get().getName();
		}				
	}
	
	public Optional<Company> getCompanyById(int id) {

		Optional<Company> company = Optional.empty();
		ArrayList<Company> buffer = Company.getCompanyList();
		
		for (Company c : buffer) {
			if (c.getId() == id) {
				return (company.ofNullable(c));
			}
		}
		return (company);
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
		result  += "{Id Company : " + this.id + " | Company Name : " + this.name + " }" + "\n";
		return (result);
	}

}
