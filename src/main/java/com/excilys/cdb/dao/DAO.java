package main.java.com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class DAO {
	
	private final String countComputer = "SELECT COUNT(*) FROM computer";
	
	private final String getAllComputers = "SELECT * FROM computer;";
	private final String getAllCompanies = "SELECT * FROM company;";
	private final String addComputer = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
	private final String getOneComputer = "SELECT * FROM computer WHERE id=?";
	private final String updateComputer = "UPDATE computer SET name = ? ,introduced = ?, discontinued = ?, company_id = ? WHERE id=?";
	private final String deleteComputer = "DELETE FROM computer WHERE id=?";
	private final String getComputerPage = "SELECT * FROM computer ORDER BY id LIMIT ? OFFSET ? ";
	private final String getCompanyPage = "SELECT * FROM company ORDER BY id LIMIT ? OFFSET ?;";
	
	private Connection db;
	
	//Singleton pattern	
	private static DAO firstDAO = new DAO();
	public static DAO getInstance() {
		return(firstDAO);
	}
	
	public ResultSet simpleRequest(String query) {
		ResultSet results = null;		
		Statement stmt;
		open();
		
		try {
			
			stmt = db.createStatement();
			results = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return(results);		
		
	}
	
	public ResultSet countComputer() {
		return(simpleRequest(countComputer));
	}

	public ResultSet listComputer(){
		return(simpleRequest(getAllComputers));
	}
	
	public ResultSet listCompany(){
		return(simpleRequest(getAllCompanies));
	}

	
	public boolean addComputer(String name, LocalDate start, LocalDate end, int company_id){
		
		int results;
		open();
		
		try {
			
			PreparedStatement ps = db.prepareStatement(addComputer);
			ps.setString(1, name);
			ps.setInt(4, company_id);
			
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
			
			stop();
			return true;
			
		} catch(Exception e){
			e.printStackTrace();
			stop();
			return false;
		}
	}
	
	public ResultSet findComputer(int id){		

		ResultSet results = null;
		open();
		
		try {
			PreparedStatement ps = db.prepareStatement(getOneComputer);
			ps.setInt(1, id);
			results = ps.executeQuery();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Computer not found");
			//A logger
		}
		
		return(results);
		
	}
	
	public boolean updateComputer(int id, String name, LocalDate start, LocalDate end, int company_id){
		
		int results;
		open();
		
		try {
			
			PreparedStatement ps = db.prepareStatement(updateComputer);
			ps.setString(1,name);
			ps.setInt(4, company_id);
			ps.setInt(5, id);
			
			
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
			stop();
			return true;
			
		} catch(Exception e){
			//A logger
			System.out.println("Computer not Found");
			e.printStackTrace();
			stop();
			return false;
		}
	}
	
	public boolean deleteComputer(int id){

		int results;
		open();
		
		try {
					
			PreparedStatement ps = db.prepareStatement(deleteComputer);
			ps.setInt(1,id);
			results = ps.executeUpdate();
			
			stop();
			return true;
			
		}catch (SQLException e) {
			//A logger
			e.printStackTrace();
			System.out.println("Computer not found");
			
			stop();
			return false;
		}
	}
	
	public ResultSet getPageComputer (int start, int taille){
		
		ResultSet results = null;
		open();
		
		try {
			PreparedStatement ps = db.prepareStatement(getComputerPage);
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = ps.executeQuery();
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return(results);
	}
	
	public ResultSet getPageCompany (int start, int taille){
		
		ResultSet results = null;
		open();
		
		try {
			PreparedStatement ps = db.prepareStatement(getCompanyPage);
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = ps.executeQuery();
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return(results);
	}
	
	public void open() {
		// URL de la source de donnée String 
		String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db" ;

		try {
			db = DriverManager.getConnection(url,"admincdb","qwerty1234");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
