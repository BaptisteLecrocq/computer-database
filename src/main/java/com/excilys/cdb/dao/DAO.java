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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.beans.CompanyBeanDb;
import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.beans.RequestParameterBeanDb;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.exception.TransactionException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTOdb;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.RequestParameter;

@Repository
public class DAO {
	
	private static final String getComputer = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name ";
	private static final String getCompany = "SELECT id, name ";
	private static final String countComputer = "SELECT COUNT(computer.id) ";
	private static final String countCompany = "SELECT COUNT(company.id) ";
	private static final String allComputers = "FROM computer LEFT JOIN company ON computer.company_id = company.id ";
	private static final String allCompanies = "FROM company ";
	private static final String searchComputer = " WHERE computer.name LIKE ? ";
	private static final String searchCompany = " WHERE company.name LIKE ? ";
	private static final String pageModel = "LIMIT ? OFFSET ?;";	
	
	private static final String getLastComputerId = "SELECT max(id) FROM computer;";
	private static final String getLastCompanyId = "SELECT max(id) FROM company;";
	private static final String addComputer = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
	private static final String addCompany = "INSERT INTO company (name) VALUES(?)";
	private static final String getOneComputer = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id=?";
	private static final String updateComputer = "UPDATE computer SET name = ? ,introduced = ?, discontinued = ?, company_id = ? WHERE id=?";
	private static final String deleteComputer = "DELETE FROM computer WHERE id=?";
	private static final String deleteCompany = "DELETE FROM company WHERE id=?";
	private static final String deleteComputers = "DELETE FROM computer WHERE id IN(";
	private static final String deleteLinkedComputers ="DELETE FROM computer WHERE company_id=?";
	
	private static final String orderBy = "ORDER BY ";
	private static final String[] order = { "ASC ", "DESC "};
	private static final String[] columnComputer = { "computer.id ", "computer.name ", "computer.introduced ", "computer.discontinued ", "company.name " };
	private static final String[] columnCompany = { "company.id ", "company.name " };
	
	private static Connection con;
	
	@Autowired
	private Database db;
	@Autowired
	private Mapper map;
	@Autowired
	private MapperDTOdb mapDb;
	
	private static Logger logger = LoggerFactory.getLogger(DAO.class);
	
	
	
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
			e.printStackTrace();
		}
		
		return(results);	
		
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
		
		CompanyBeanDb cBean = mapDb.mapCompanyModelToDTOdb(company);
		
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
			
			ps.executeUpdate();
			return true;
			
		} catch(SQLException e){
			
			logger.error(e.toString());
			return false;
		}
	}
	
	public boolean deleteComputer(int id){

		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(deleteComputer) ) {
					
			ps.setInt(1,id);
			ps.executeUpdate();
			
			return true;
			
		}catch (SQLException e) {
			logger.error(e.toString());			
			return false;
		}
	}
	
	public void deleteCompany(int id) throws TransactionException {
		
		try ( Connection con = db.getConnection() ){		
		
			try (	PreparedStatement ps = con.prepareStatement(deleteLinkedComputers); 
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
			
		} catch (SQLException e2) {
			logger.error(e2.toString());
			e2.printStackTrace();
		}
		
	}
	
	public void deleteComputerList(String list) {
		
		String[] buffer = list.split(",");
		String query = deleteComputers;
		ArrayList<Integer> values = new ArrayList<Integer>();

		values.add(Integer.parseInt(buffer[0]));
		
		for(int i=1; i<buffer.length; i++) {
			
			query+="?,";
			values.add(Integer.parseInt(buffer[i]));
			
		}
		query+="?)";
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query) ) {
			
			for(int i = 0; i < values.size(); i++) {
				ps.setInt(i, (int) values.get(i));
			}
			
			ps.executeUpdate();
		
			
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		
	}
	
	
	/*            Composed Requests             */	
	
	public ArrayList<Computer> listComputer(RequestParameter parameters){
			
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
	
	public ArrayList<Company> listCompany(RequestParameter parameters){
			
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

	
	public int countComputer(RequestParameter parameters) {
			
		Optional<ResultSet> results = Optional.empty();
		int count = 0;
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(countComputer+allComputers+searchComputer+";") ) {
			
			ps.setString(1, "%"+parameters.getSearchTerm()+"%");
			results = Optional.ofNullable(ps.executeQuery());
			count = map.countComputer(results);
					
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
					
		return(count);
	}			
	
	public int countCompany(RequestParameter parameters) {
			
		Optional<ResultSet> results = Optional.empty();
		int count = 0;
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(countCompany+allCompanies+searchCompany+";") ) {
			
			ps.setString(1, "%"+parameters.getSearchTerm()+"%");
			results = Optional.ofNullable(ps.executeQuery());
			count = map.countCompany(results);
					
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
					
		return(count);
	}			
	
	public ArrayList<Computer> pageComputer(Page page){
		
		Optional<ResultSet> results = Optional.empty();
		ArrayList<Computer> searchResult = new ArrayList<Computer>(); 
		RequestParameterBeanDb parameters = mapDb.mapParametersToDTOdb(page.getParameters());
		
		String query = getComputer+allComputers+searchComputer+orderBy+columnComputer[parameters.getChoice()]+order[parameters.getOrder()]+pageModel;
		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query) ) {
			
			ps.setString(1, "%"+parameters.getSearchTerm()+"%");
			ps.setInt(2, page.getTaille());
			ps.setInt(3, page.getStart());
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
	
	public ArrayList<Company> pageCompany(Page page){
			
		Optional<ResultSet> results = Optional.empty();
		ArrayList<Company> searchResult = new ArrayList<Company>();
		RequestParameterBeanDb parameters = mapDb.mapParametersToDTOdb(page.getParameters());
		
		String query = getCompany+allCompanies+searchCompany+orderBy+columnCompany[parameters.getChoice()]+order[parameters.getOrder()]+pageModel;

		
		try ( Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query) ) {
			
			ps.setString(1, "%"+parameters.getSearchTerm()+"%");
			ps.setInt(2, page.getTaille());
			ps.setInt(3, page.getStart());
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
	
	/*            Utility             */
	
 	public Connection getCon() {
		return con;
	}	

	public Database getDb() {
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
