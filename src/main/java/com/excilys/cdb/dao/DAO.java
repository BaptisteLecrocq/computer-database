package com.excilys.cdb.dao;

import java.sql.Connection;  
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTOdb;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;


public class DAO {
	
	private final String countComputer = "SELECT COUNT(*) FROM computer";
	private final String countCompany = "SELECT COUNT(*) FROM company";
	private final String getLastComputerId = "SELECT max(id) FROM computer;";
	
	private final String getAllComputers = "SELECT id,name,introduced,discontinued,company_id FROM computer;";
	private final String getAllCompanies = "SELECT id,name FROM company;";
	private final String addComputer = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
	private final String getOneComputer = "SELECT id,name,introduced,discontinued,company_id FROM computer WHERE id=?";
	private final String updateComputer = "UPDATE computer SET name = ? ,introduced = ?, discontinued = ?, company_id = ? WHERE id=?";
	private final String deleteComputer = "DELETE FROM computer WHERE id=?";
	private final String getComputerPage = "SELECT id,name,introduced,discontinued,company_id FROM computer ORDER BY id LIMIT ? OFFSET ? ";
	private final String getCompanyPage = "SELECT id,name FROM company ORDER BY id LIMIT ? OFFSET ?;";
	
	private static Connection con;
	private static Database db = Database.getInstance();
	private Mapper map = Mapper.getInstance();
	private MapperDTOdb mapDb = MapperDTOdb.getInstance();
	
	private static Logger logger = LoggerFactory.getLogger(DAO.class);
	
	
	//Singleton pattern	
	private static DAO firstDAO = new DAO();
	public static DAO getInstance() {
		return (firstDAO);
	}
	
	private DAO() {}
	
	public Optional<ResultSet> simpleRequest(String query) {
		Optional<ResultSet> results = Optional.empty();	
		open();
		
		try (Statement stmt = con.createStatement();){
			
			results = Optional.ofNullable(stmt.executeQuery(query));
			
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		stop();
		return(results);		
		
	}
	
	public int countComputer() {
		
		Optional<ResultSet> results = simpleRequest(countComputer);		
		return(map.countComputer(results));
	}
	
	public int countCompany() {
		
		Optional<ResultSet> results = simpleRequest(countCompany);
		return(map.countCompany(results));
	}

	public ArrayList<Computer> listComputer(){
		
		Optional<ResultSet> results = simpleRequest(getAllComputers);
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		
		try {
			 listComputer = map.mapComputerList(results);
		} catch (NotFoundException e) {
			logger.info(e.toString());
		}
		return(listComputer);
	}
	
	public ArrayList<Company> listCompany(){
		
		Optional<ResultSet> results = simpleRequest(getAllCompanies);
		ArrayList<Company> listCompany = new ArrayList<Company>();
		
		try {
			listCompany = map.mapCompanyList(results);
		} catch (NotFoundException e) {
			logger.info(e.toString());
		}
		return(listCompany);
	}

	public int getLastComputerId() {
		
		Optional<ResultSet> results = simpleRequest(getLastComputerId);
		return(map.countComputer(results));
	}
	
	
	public boolean addComputer(Computer computer){
		
		
		ComputerBeanDb cBean = mapDb.mapModelToDTOdb(computer);
		open();
		
		try ( PreparedStatement ps = con.prepareStatement(addComputer) ){			
			
			ps.setString(1, cBean.getName());
			ps.setInt(4, cBean.getCompanyId());
			
			Optional<String> start = Optional.ofNullable(cBean.getIntroduced());
			Optional<String> end = Optional.ofNullable(cBean.getDiscontinued());
			
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
	
	public Optional<Computer> findComputer(int id){		

		Optional<ResultSet> results = Optional.empty();	
		Optional<Computer> computer = Optional.empty();
		open();
		
		try ( PreparedStatement ps = con.prepareStatement(getOneComputer) ) {

			ps.setInt(1, id);
			results = Optional.ofNullable(ps.executeQuery());
			computer = map.getOneComputer(results);
			
		}catch(SQLException e) {
			logger.error(e.toString());
			
		}catch(NotFoundException e) {
			logger.info(e.toString());
		}
		
		stop();
		return(computer);
		
	}
	
	public boolean updateComputer(Computer computer){

		ComputerBeanDb cBean = mapDb.mapModelToDTOdb(computer);
		open();
		
		try ( PreparedStatement ps = con.prepareStatement(updateComputer) ) {
			
			ps.setString(1,cBean.getName());
			ps.setInt(4, cBean.getCompanyId());
			ps.setInt(5, cBean.getId());
			
			Optional<String> start = Optional.ofNullable(cBean.getIntroduced());
			Optional<String> end = Optional.ofNullable(cBean.getDiscontinued());
			
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
		
		try ( PreparedStatement ps = con.prepareStatement(deleteComputer) ) {
					
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
	
	public ArrayList<Computer> getPageComputer (int start, int taille){
		
		Optional<ResultSet> results = Optional.empty();
		ArrayList<Computer> pageComputer = new ArrayList<Computer>();
		open();
		
		try ( PreparedStatement ps = con.prepareStatement(getComputerPage) ) {
		
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = Optional.ofNullable(ps.executeQuery());
			pageComputer = map.mapComputerList(results);
					
		} catch (SQLException e) {
			logger.error(e.toString());
			
		} catch (NotFoundException e) {
			logger.info(e.toString());
		}
		
		stop();
		return(pageComputer);
	}
	
	public ArrayList<Company> getPageCompany (int start, int taille){
		
		Optional<ResultSet> results = Optional.empty();
		ArrayList<Company> pageCompany = new ArrayList<Company>();		
		open();
		
		try ( PreparedStatement ps = con.prepareStatement(getCompanyPage) ) {
			
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = Optional.ofNullable(ps.executeQuery());
			pageCompany = map.mapCompanyList(results);
					
		} catch (SQLException e) {
			logger.error(e.toString());
			
		} catch (NotFoundException e) {
			logger.info(e.toString());
		}
		
		stop();
		return(pageCompany);
	}

	public static Connection getCon() {
		return con;
	}

	public static Database getDb() {
		return db;
	}
	
	
	public void open() {

		try {
			
			Class.forName(Database.getDriver());
			con = DriverManager.getConnection(Database.getUrl(),Database.getUsername(),Database.getPassword());
			
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {

		try {
			con.close();
		} catch (SQLException e) {
			logger.error(e.toString());
		}
	}

}
