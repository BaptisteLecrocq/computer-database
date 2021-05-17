package com.excilys.dao;

import com.excilys.model.*;
import com.excilys.service.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDate;

public class Connect {
	
	private Connection db;
	
	public Connect(){	
		
		// URL de la source de donnée String 
		String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db" ;

		try {
			db = DriverManager.getConnection(url,"admincdb","qwerty1234");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public ArrayList<Computer> listComputer() {
		
		String query = "SELECT * FROM computer;";
		ResultSet results = null;
		ArrayList<Computer> list = new ArrayList<Computer>();
		
		try {
			Statement stmt = db.createStatement();
			results = stmt.executeQuery(query);
			
			while(results.next()) {
				
				Computer buffer = new Computer(results.getInt("computer.id"),results.getString("computer.name"),results.getObject("computer.introduced",LocalDate.class),results.getObject("computer.discontinued",LocalDate.class),results.getInt("computer.company_id"));
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
		
		try {
			Statement stmt = db.createStatement();
			results = stmt.executeQuery(query);
			
			while(results.next()) {
				
				Company buffer = new Company(results.getInt("company.id"), results.getString("company.name"));				
				list.add(buffer);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return(list);
		
	}

	public void addComputer(Computer computer) {
		
		String query = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
		int results;
		
		try {
			
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, computer.getName());
			ps.setInt(4, computer.getManufacturer().getId());
			
			LocalDate start = computer.getStart();
			LocalDate end = computer.getEnd();
			
			if(start==null) {
				ps.setNull(2, 0);
				
			}else {
				ps.setDate(2, Date.valueOf(start));
			}
			
			if(end==null) {
				ps.setNull(3, 0);
				
			} else {
				ps.setDate(3, Date.valueOf(end));
			}
			
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
			computer = new Computer(results.getInt("id"),results.getString("name"),results.getObject("introduced",LocalDate.class),results.getObject("discontinued",LocalDate.class),results.getInt("company_id"));
		
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
			ps.setInt(4, computer.getManufacturer().getId());
			ps.setInt(5, id);
			
			LocalDate start = computer.getStart();
			LocalDate end = computer.getEnd();
			
			if(start==null) {
				ps.setNull(2, 0);
				
			}else {
				ps.setDate(2, Date.valueOf(start));
			}
			
			if(end==null) {
				ps.setNull(3, 0);
				
			} else {
				ps.setDate(3, Date.valueOf(end));
			}
			
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
	
	public ArrayList<Computer> getPageComputer (int start, int taille){
		
		ArrayList<Computer> list = new ArrayList<Computer>(taille);
		
		String query = "SELECT * FROM computer ORDER BY id LIMIT ? OFFSET ? ";
		ResultSet results;
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = ps.executeQuery();
			
			while(results.next()) {
				
				Computer buffer = new Computer(results.getInt("computer.id"),
					results.getString("computer.name"),
					results.getObject("computer.introduced",LocalDate.class),
					results.getObject("computer.discontinued",LocalDate.class),
					results.getInt("computer.company_id"));
				
				list.add(buffer);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return(list);
	}
	
	public ArrayList<Company> getPageCompany (int start, int taille){
		
		ArrayList<Company> list = new ArrayList<Company>(taille);
		
		String query = "SELECT * FROM computer ORDER BY id LIMIT ? OFFSET ?;";
		ResultSet results;
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = ps.executeQuery();
			
			while(results.next()) {
				
				Company buffer = new Company(results.getInt("company.id"),results.getString("company.name"));
				list.add(buffer);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return(list);
	}
	
	public void stop() {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
