package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DAO {
	
	private final String countComputer = "SELECT COUNT(*) FROM computer";
	
	private final String getAllComputers = "SELECT id,name,introduced,discontinued,company_id FROM computer;";
	private final String getAllCompanies = "SELECT id,name FROM company;";
	private final String addComputer = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
	private final String getOneComputer = "SELECT id,name,introduced,discontinued,company_id FROM computer WHERE id=?";
	private final String updateComputer = "UPDATE computer SET name = ? ,introduced = ?, discontinued = ?, company_id = ? WHERE id=?";
	private final String deleteComputer = "DELETE FROM computer WHERE id=?";
	private final String getComputerPage = "SELECT id,name,introduced,discontinued,company_id FROM computer ORDER BY id LIMIT ? OFFSET ? ";
	private final String getCompanyPage = "SELECT id,name FROM company ORDER BY id LIMIT ? OFFSET ?;";
	
	private Connection db;
	
	private static Logger logger = LoggerFactory.getLogger(DAO.class);
	
	
	//Singleton pattern	
	private static DAO firstDAO = new DAO();
	public static DAO getInstance() {
		return (firstDAO);
	}
	
	public Optional<ResultSet> simpleRequest(String query) {
		Optional<ResultSet> results = Optional.empty();	
		Statement stmt;
		open();
		
		try {
			
			stmt = db.createStatement();
			results = Optional.ofNullable(stmt.executeQuery(query));
			
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		return(results);		
		
	}
	
	public Optional<ResultSet> countComputer() {
		return(simpleRequest(countComputer));
	}

	public Optional<ResultSet> listComputer(){
		return(simpleRequest(getAllComputers));
	}
	
	public Optional<ResultSet> listCompany(){
		return(simpleRequest(getAllCompanies));
	}

	
	public boolean addComputer(String name, Optional<LocalDate> start, Optional<LocalDate> end, int company_id){
		
		open();
		
		try {
			
			PreparedStatement ps = db.prepareStatement(addComputer);
			ps.setString(1, name);
			ps.setInt(4, company_id);
			
			if(!start.isPresent()) {
				ps.setNull(2, 0);
				
			}else {
				ps.setDate(2, Date.valueOf(start.get()));
			}
			
			if(!end.isPresent()) {
				ps.setNull(3, 0);
				
			} else {
				ps.setDate(3, Date.valueOf(end.get()));
			}
			
			int results = ps.executeUpdate();
			
			stop();
			return true;
			
		} catch(SQLException e){
			logger.error(e.toString());
			stop();
			return false;
		}
	}
	
	public Optional<ResultSet> findComputer(int id){		

		Optional<ResultSet> results = Optional.empty();	
		open();
		
		try {
			PreparedStatement ps = db.prepareStatement(getOneComputer);
			ps.setInt(1, id);
			results = Optional.ofNullable(ps.executeQuery());
			
		}catch(SQLException e) {
			
			logger.error(e.toString());
			
		}
		
		return(results);
		
	}
	
	public boolean updateComputer(int id, String name, Optional<LocalDate> start, Optional<LocalDate> end, int company_id){

		open();
		
		try {
			
			PreparedStatement ps = db.prepareStatement(updateComputer);
			ps.setString(1,name);
			ps.setInt(4, company_id);
			ps.setInt(5, id);
			
			
			if(!start.isPresent()) {
				ps.setNull(2, 0);
				
			}else {
				ps.setDate(2, Date.valueOf(start.get()));
			}
			
			if(!end.isPresent()) {
				ps.setNull(3, 0);
				
			} else {
				ps.setDate(3, Date.valueOf(end.get()));
			}
			
			int results = ps.executeUpdate();
			stop();
			return true;
			
		} catch(SQLException e){
			
			logger.error(e.toString());
			stop();
			return false;
		}
	}
	
	public boolean deleteComputer(int id){

		open();
		
		try {
					
			PreparedStatement ps = db.prepareStatement(deleteComputer);
			ps.setInt(1,id);
			int results = ps.executeUpdate();
			
			stop();
			return true;
			
		}catch (SQLException e) {
			logger.error(e.toString());			
			stop();
			return false;
		}
	}
	
	public Optional<ResultSet> getPageComputer (int start, int taille){
		
		Optional<ResultSet> results = Optional.empty();
		open();
		
		try {
			PreparedStatement ps = db.prepareStatement(getComputerPage);
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = Optional.ofNullable(ps.executeQuery());
					
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		return(results);
	}
	
	public Optional<ResultSet> getPageCompany (int start, int taille){
		
		Optional<ResultSet> results = Optional.empty();
		open();
		
		try {
			PreparedStatement ps = db.prepareStatement(getCompanyPage);
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = Optional.ofNullable(ps.executeQuery());
					
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		return(results);
	}
	
	public void open() {

		// URL de la source de donnée String 
		String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db" ;

		try {
			db = DriverManager.getConnection(url,"admincdb","qwerty1234");
		
		} catch (SQLException e) {
			logger.error(e.toString());
		}
	}
	
	public void stop() {

		try {
			db.close();
		} catch (SQLException e) {
			logger.error(e.toString());
		}
	}

}
