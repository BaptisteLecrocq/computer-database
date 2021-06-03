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

import com.excilys.cdb.beans.CompanyBeanDb;
import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.exception.TransactionException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTOdb;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.RequestParameter;


public class DAO {
	
	private static final String getComputer = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name ";
	private static final String getCompany = "SELECT id, name ";
	private static final String doCount = "SELECT COUNT(computer.id) ";
	private static final String allComputers = "FROM computer LEFT JOIN company ON computer.company_id = company.id ";
	private static final String allCompanies = "FROM company ";
	private static final String searchComputer = " WHERE computer.name LIKE ? ";
	private static final String searchCompany = " WHERE company.name LIKE ? ";
	private static final String page = " LIMIT ? OFFSET ?;";	
	
	private static final String getLastComputerId = "SELECT max(id) FROM computer;";
	private static final String getLastCompanyId = "SELECT max(id) FROM company;";
	private static final String addComputer = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
	private static final String addCompany = "INSERT INTO company (name) VALUES(?)";
	private static final String getOneComputer = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id=?";
	private static final String updateComputer = "UPDATE computer SET name = ? ,introduced = ?, discontinued = ?, company_id = ? WHERE id=?";
	private static final String deleteComputer = "DELETE FROM computer WHERE id=?";
	private static final String deleteCompany = "DELETE FROM company WHERE id=?";
	private static final String deleteComputers ="DELETE FROM computer WHERE company_id=?";
	
	private static final String getComputerPage = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name FROM computer LEFT JOIN company ON computer.company_id = company.id ORDER BY id LIMIT ? OFFSET ? ";
	private static final String getCompanyPage = "SELECT id,name FROM company ORDER BY id LIMIT ? OFFSET ?;";
	
	private static final String searchComputerList = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name \n"
			+"FROM computer LEFT JOIN company ON computer.company_id = company.id \n"
			+"WHERE computer.name LIKE %?%";
	
	private static final String orderBy = "ORDER BY ";
	private static final String[] order = { "", "ASC ", "DESC "};
	private static final String[] column = { "computer.name ", "computer.introduced ", "computer.discontinued ", "company.name " };
	
	
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
	
	/*            Simple Requests             */
	
	public Optional<ResultSet> simpleRequest(String query) {
		Optional<ResultSet> results = Optional.empty();
		Statement stmt;		
		
		try {
			
			con = db.getConnection();
			stmt = con.createStatement();
			results = Optional.ofNullable(stmt.executeQuery(query));
			
		} catch (SQLException e) {
			logger.error(e.toString());
		}
		
		return(results);	
		
	}
	
	public int countAllComputer() {
		
		Optional<ResultSet> results = simpleRequest(doCount+allComputers);
		int count = map.countComputer(results);
		
		close();
		return(count);
	}
	
	public int countAllCompany() {
		
		Optional<ResultSet> results = simpleRequest(doCount+allCompanies);
		int count = map.countCompany(results);
		
		close();
		return(count);
	}

	public ArrayList<Computer> listAllComputer(RequestParameter parameters){
		
		Optional<ResultSet> results = simpleRequest(getComputer+allComputers+column[parameters.getChoice()]+order[parameters.getOrder()]+";");
		ArrayList<Computer> listComputer = new ArrayList<Computer>();
		
		try {
			 listComputer = map.mapComputerList(results);
		} catch (NotFoundException e) {
			logger.info(e.toString());
		}
		
		close();
		return(listComputer);
	}
	
	public ArrayList<Company> listAllCompany(RequestParameter parameters){
		
		Optional<ResultSet> results = simpleRequest(getCompany+allCompanies+column[parameters.getChoice()]+order[parameters.getOrder()]+";");
		ArrayList<Company> listCompany = new ArrayList<Company>();
		
		try {
			listCompany = map.mapCompanyList(results);
		} catch (NotFoundException e) {
			logger.info(e.toString());
		}
		
		close();
		return(listCompany);
	}

	public int getLastComputerId() {
		
		Optional<ResultSet> results = simpleRequest(getLastComputerId);
		int id = map.countComputer(results);
		
		close();
		return(id);
	}
	
	public int getLastCompanyId() { 
		
		Optional<ResultSet> results = simpleRequest(getLastCompanyId);
		int id = map.countCompany(results);
		
		close();
		return(id);
	}
	
	
	/*            CRUD Requests             */	
	
	public boolean addComputer(Computer computer){
		
		
		ComputerBeanDb cBean = mapDb.mapComputerModelToDTOdb(computer);
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(addComputer) ){			
			
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
			
			ps.executeUpdate();
			
			return true;
			
		} catch(SQLException e){
			logger.error(e.toString());
			return false;
		}
	}
	
	public void addCompany(Company company) {
		
		CompanyBeanDb cBean = mapDb.mapCompanyModeltoDTOdb(company);
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(addCompany) ){
			
			if( cBean.getName() == null) {
				ps.setNull(1, 0);
				
			} else {
				ps.setString(1, cBean.getName());
			}
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		
	}

	public Optional<Computer> findComputer(int id){		

		Optional<ResultSet> results = Optional.empty();	
		Optional<Computer> computer = Optional.empty();
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(getOneComputer) ) {

			ps.setInt(1, id);
			results = Optional.ofNullable(ps.executeQuery());
			computer = map.getOneComputer(results);
			
		}catch(SQLException e) {
			logger.error(e.toString());
			
		}catch(NotFoundException e) {
			logger.info(e.toString());
		}
		
		return(computer);
		
	}
	
	public boolean updateComputer(Computer computer){

		ComputerBeanDb cBean = mapDb.mapComputerModelToDTOdb(computer);
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(updateComputer) ) {
			
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
			return true;
			
		} catch(SQLException e){
			
			logger.error(e.toString());
			return false;
		}
	}
	
	public boolean deleteComputer(int id){

		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(deleteComputer) ) {
					
			ps.setInt(1,id);
			int results = ps.executeUpdate();
			
			return true;
			
		}catch (SQLException e) {
			logger.error(e.toString());			
			return false;
		}
	}
	
	public void deleteCompany(int id) throws TransactionException {
		
		try ( Connection con = db.getConnection(); 
				PreparedStatement ps = con.prepareStatement(deleteComputers); 
				PreparedStatement ps2 = con.prepareStatement(deleteCompany) ){
			
			con.setAutoCommit(false);
			
			ps.setInt(1, id);
			ps.executeUpdate();
			ps2.setInt(1, id);
			ps2.executeUpdate();
			
			con.commit();
			
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
			
			try {
				con.rollback();
				
			} catch (SQLException e1) {
				logger.info(e1.toString());
				e1.printStackTrace();
				throw new TransactionException("Delete Company Transaction failed");
			}
			
		} 
		
	}
	
	
	/*            Page Requests             */	

	public ArrayList<Computer> pageAllComputer (int start, int taille, RequestParameter parameters){
		
		Optional<ResultSet> results = Optional.empty();
		ArrayList<Computer> pageComputer = new ArrayList<Computer>();
		
		String query = getComputer+allComputers+orderBy+column[parameters.getChoice()]+order[parameters.getOrder()]+page;
		System.out.println(query);
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query) ) {
		
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = Optional.ofNullable(ps.executeQuery());
			pageComputer = map.mapComputerList(results);
					
		} catch (SQLException e) {
			logger.error(e.toString());
			
		} catch (NotFoundException e) {
			logger.info(e.toString());
		}
		
		return(pageComputer);
	}
	
	public ArrayList<Company> pageAllCompany (int start, int taille, RequestParameter parameters){
		
		Optional<ResultSet> results = Optional.empty();
		ArrayList<Company> pageCompany = new ArrayList<Company>();
		
		String query = getCompany+allCompanies+orderBy+column[parameters.getChoice()]+order[parameters.getOrder()]+page;
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query) ) {
			
			ps.setInt(1, taille);
			ps.setInt(2, start);
			results = Optional.ofNullable(ps.executeQuery());
			pageCompany = map.mapCompanyList(results);
					
		} catch (SQLException e) {
			logger.error(e.toString());
			
		} catch (NotFoundException e) {
			logger.info(e.toString());
		}
		
		return(pageCompany);
	}

	
	/*            Composed Requests             */	
	
	public ArrayList<Computer> listComputer(RequestParameter parameters){
		
		if( parameters.getSearchTerm() == null) {
			return(listAllComputer(parameters));
		
		} else {
			
			Optional<ResultSet> results = Optional.empty();
			ArrayList<Computer> searchResult = new ArrayList<Computer>();
			
			try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(getComputer+allComputers+searchComputer+";") ) {
				
				ps.setString(1, "%"+parameters.getSearchTerm()+"%");
				results = Optional.ofNullable(ps.executeQuery());
				searchResult = map.mapComputerList(results);
				
				
			} catch (SQLException e) {
				logger.error(e.toString());
				e.printStackTrace();
				
			} catch (NotFoundException e) {
				logger.info(e.toString());
			}
			
			return(searchResult);
		}		
	}
	
	public ArrayList<Company> listCompany(RequestParameter parameters){
		
		if( parameters.getSearchTerm() == null) {
			return(listAllCompany(parameters));
		
		} else {
			
			Optional<ResultSet> results = Optional.empty();
			ArrayList<Company> searchResult = new ArrayList<Company>();
			
			try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(getCompany+allCompanies+searchCompany+";") ) {
				
				ps.setString(1, "%"+parameters.getSearchTerm()+"%");
				results = Optional.ofNullable(ps.executeQuery());
				searchResult = map.mapCompanyList(results);
				
				
			} catch (SQLException e) {
				logger.error(e.toString());
				e.printStackTrace();
				
			} catch (NotFoundException e) {
				logger.info(e.toString());
			}
			
			return(searchResult);
		}		
	}
	
	public int countComputer(RequestParameter parameters) {
		
		if( parameters.getSearchTerm() == null ) {
			return(countAllComputer());
		}
		else {
			
			Optional<ResultSet> results = Optional.empty();
			int count = 0;
			
			try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(doCount+allComputers+searchComputer+";") ) {
				
				ps.setString(1, "%"+parameters.getSearchTerm()+"%");
				results = Optional.ofNullable(ps.executeQuery());
				count = map.countComputer(results);
						
			} catch (SQLException e) {
				logger.error(e.toString());
				e.printStackTrace();
			}
						
			return(count);
		}			
	}
	
	public int countCompany(RequestParameter parameters) {
		
		if(parameters.getSearchTerm() == null) {
			return(countAllCompany());
		}
		else {
			
			Optional<ResultSet> results = Optional.empty();
			int count = 0;
			
			try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(doCount+allCompanies+searchCompany+";") ) {
				
				ps.setString(1, "%"+parameters.getSearchTerm()+"%");
				results = Optional.ofNullable(ps.executeQuery());
				count = map.countCompany(results);
						
			} catch (SQLException e) {
				logger.error(e.toString());
				e.printStackTrace();
			}
						
			return(count);
		}			
	}
	
	public ArrayList<Computer> pageComputer(int start, int taille, RequestParameter parameters){
		
		if( parameters.getSearchTerm() == null) {
			return(pageAllComputer(start, taille, parameters));
		
		} else {
			
			Optional<ResultSet> results = Optional.empty();
			ArrayList<Computer> searchResult = new ArrayList<Computer>();
			
			String query = getComputer+allComputers+searchComputer+orderBy+column[parameters.getChoice()]+order[parameters.getOrder()]+page;
			
			try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query) ) {
				
				ps.setString(1, "%"+parameters.getSearchTerm()+"%");
				ps.setInt(2, taille);
				ps.setInt(3, start);
				results = Optional.ofNullable(ps.executeQuery());
				searchResult = map.mapComputerList(results);
				
				
			} catch (SQLException e) {
				logger.error(e.toString());
				e.printStackTrace();
				
			} catch (NotFoundException e) {
				logger.info(e.toString());
			}
			
			return(searchResult);
		}		
	}
	
	public ArrayList<Company> pageCompany(int start, int taille, RequestParameter parameters){
		
		if( parameters.getSearchTerm() == null) {
			return(pageAllCompany(start, taille, parameters));
		
		} else {
			
			Optional<ResultSet> results = Optional.empty();
			ArrayList<Company> searchResult = new ArrayList<Company>();
			
			String query = getCompany+allCompanies+searchCompany+orderBy+column[parameters.getChoice()]+order[parameters.getOrder()]+page;

			
			try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query) ) {
				
				ps.setString(1, "%"+parameters.getSearchTerm()+"%");
				ps.setInt(2, taille);
				ps.setInt(2, start);
				results = Optional.ofNullable(ps.executeQuery());
				searchResult = map.mapCompanyList(results);
				
				
			} catch (SQLException e) {
				logger.error(e.toString());
				e.printStackTrace();
				
			} catch (NotFoundException e) {
				logger.info(e.toString());
			}
			
			return(searchResult);
		}		
	}
	
	/*            Utility             */	
	
 	public static Connection getCon() {
		return con;
	}	

	public static Database getDb() {
		return db;
	}
	
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
	}

}
