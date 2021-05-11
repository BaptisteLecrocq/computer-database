package com.excilys.dao;

import com.excilys.model.*;
import com.excilys.service.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Connect {
	
	private Connection db;

	//Singleton pattern
	/*
	private static Connect Bdd = new Connect();
	private static Connect getBdd() {
		return(Bdd);		
	}
	*/			
	
	public Connect(){
		// Chargement du Driver et enregistrement auprès du DriverMgr
		/*
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/		
		
		// URL de la source de donnéeString 
		String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db" ;

		try {
			db = DriverManager.getConnection(url,"admincdb","qwerty1234");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public ArrayList<Computer> listComputer() {
		
		String query = "SELECT * FROM computer;";
		ResultSet results = null;
		ArrayList<Computer> list = new ArrayList<Computer>();
		int id;
		String name;
		Date start;
		Date end;
		int manufacturer;
		
		
		try {
			Statement stmt = db.createStatement();
			results = stmt.executeQuery(query);
			
			while(results.next()) {
				
				id = results.getInt("computer.id");
				name = results.getString("computer.name");
				start = results.getDate("computer.introduced");
				end = results.getDate("computer.discontinued");
				manufacturer = results.getInt("computer.company_id");
				
				Computer buffer = new Computer(id,name,start,end,manufacturer);
				
				list.add(buffer);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return(list);
		
	}
	
public ArrayList<Company> listCompany() {
		
		String query = "SELECT * FROM company;";
		ResultSet results = null;
		ArrayList<Company> list = new ArrayList<Company>();
		
		int id;
		String name;		
		
		try {
			Statement stmt = db.createStatement();
			results = stmt.executeQuery(query);
			
			while(results.next()) {
				
				id = results.getInt("company.id");
				name = results.getString("company.name");
				
				Company buffer = new Company(id, name);				
				list.add(buffer);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return(list);
		
	}
	
	/*
	public void add(Computer computer) {
		
	}
	*/

	public void addComputer(Computer computer) {
		
		String query = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
		//String query = "INSERT INTO company('name','introduced','discontinued','company_id') VALUES('"+computer.getName()+"','"+computer.getStart()+"','"+computer.getEnd()+"','"+computer.getManufacturer().getId()+"')";
		int results;
		
		try {
			
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1,computer.getName());
			ps.setDate(2, (java.sql.Date) computer.getStart());
			ps.setDate(3, (java.sql.Date) computer.getEnd());
			ps.setInt(4, computer.getManufacturer().getId());
			results = ps.executeUpdate();
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Computer findComputer(String name) {
		
		String query = "SELECT * FROM computer WHERE name=?";
		ResultSet results = null;
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, name);
			results = ps.executeQuery();
			results.next();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Computer computer = null;
		try {
			computer = new Computer(results.getInt("id"),results.getString("name"),results.getDate("introduced"),results.getDate("discontinued"),results.getInt("company_id"));
		
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Computer not found");
		}
		
		return(computer);
		
	}
	
	public void updateComputer(int id, Computer computer) {
		String query = "UPDATE computer SET name = ? ,introduced = ?, discontinued = ?, company_id = ? WHERE id=?";
		//String query = "INSERT INTO company('name','introduced','discontinued','company_id') VALUES('"+computer.getName()+"','"+computer.getStart()+"','"+computer.getEnd()+"','"+computer.getManufacturer().getId()+"')";
		int results;
		
		try {
			
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1,computer.getName());
			ps.setDate(2, (java.sql.Date) computer.getStart());
			ps.setDate(3, (java.sql.Date) computer.getEnd());
			ps.setInt(4, computer.getManufacturer().getId());
			ps.setInt(5, id);
			results = ps.executeUpdate();
			
		} catch(Exception e){
			System.out.println("DAO : Computer not Found");
			e.printStackTrace();
		}
	}
	
	public void deleteComputer(int id) {
		String query = "DELETE FROM computer WHERE id=?";
		int results;
		
		try {
					
			PreparedStatement ps = db.prepareStatement(query);
			ps.setInt(1,id);
			results = ps.executeUpdate();
			
		}catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Computer not found");
		}
	}
	
	public void stop() {
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
