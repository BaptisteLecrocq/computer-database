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
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.beans.CompanyBeanDb;
import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.beans.RequestParameterBeanDb;
import com.excilys.cdb.dao.JDBCMapper.CompanyRowMapper;
import com.excilys.cdb.dao.JDBCMapper.ComputerRowMapper;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.exception.TransactionException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTOdb;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.RequestParameter;

@Repository
public class DAO{
	
	private static final String getComputer = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name ";
	private static final String getCompany = "SELECT id, name ";
	private static final String countComputer = "SELECT COUNT(computer.id) ";
	private static final String countCompany = "SELECT COUNT(company.id) ";
	private static final String allComputers = "FROM computer LEFT JOIN company ON computer.company_id = company.id ";
	private static final String allCompanies = "FROM company ";
	private static final String searchComputer = " WHERE computer.name LIKE :searchTerm ";
	private static final String searchCompany = " WHERE company.name LIKE :searchTerm ";
	private static final String pageModel = "LIMIT :size OFFSET :start ;";
	
	private static final String getLastComputerId = "SELECT max(id) FROM computer;";
	private static final String getLastCompanyId = "SELECT max(id) FROM company;";
	private static final String addComputer = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(:name,:introduced,:discontinued,:companyId)";
	private static final String addCompany = "INSERT INTO company (name) VALUES(:id)";
	private static final String getOneComputer = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name FROM computer LEFT JOIN company ON computer.company_id = company.id WHERE computer.id=:id";
	private static final String updateComputer = "UPDATE computer SET name = :name ,introduced = :introduced, discontinued = :discontinued, company_id = :companyId WHERE id=:id";
	private static final String deleteComputer = "DELETE FROM computer WHERE id=:id";
	private static final String deleteCompany = "DELETE FROM company WHERE id=?";
	private static final String deleteComputers = "DELETE FROM computer WHERE id IN(";
	private static final String deleteLinkedComputers ="DELETE FROM computer WHERE company_id=?";
	
	private static final String orderBy = "ORDER BY ";
	private static final String[] order = { "ASC ", "DESC "};
	private static final String[] columnComputer = { "computer.id ", "computer.name ", "computer.introduced ", "computer.discontinued ", "company.name " };
	private static final String[] columnCompany = { "company.id ", "company.name " };
	
	
	private ComputerRowMapper computerMapper;
	private CompanyRowMapper companyMapper;
	private Database db;
	private Mapper map;
	private MapperDTOdb mapDb;
	
	private static Logger logger = LoggerFactory.getLogger(DAO.class);
	
	public DAO(ComputerRowMapper computerMapper, CompanyRowMapper companyMapper, Database db, Mapper map, MapperDTOdb mapDb) {
		
		this.computerMapper = computerMapper;
		this.companyMapper = companyMapper;
		this.db = db;
		this.map = map;
		this.mapDb = mapDb;
		
	}
	
	/*            Simple Requests             */

 	public int getLastComputerId() {
		
		JdbcTemplate temp = new JdbcTemplate(db.getDataSource());		
		int id = temp.queryForObject(getLastComputerId, Integer.class);

		return(id);
	}
	
	
	public int getLastCompanyId() { 
		
		
		JdbcTemplate temp = new JdbcTemplate(db.getDataSource());		
		int id = temp.queryForObject(getLastCompanyId, Integer.class);

		return(id);
	}
	
	
	/*            CRUD Requests             */	
	
	public boolean addComputer(Computer computer){
		
		ComputerBeanDb cBean = mapDb.mapComputerModelToDTOdb(computer);
		
		SqlParameterSource params = new BeanPropertySqlParameterSource(cBean);
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		
		temp.update(addComputer, params);
		
		return true;
		
	}
	
	public void addCompany(Company company) {
		
		CompanyBeanDb cBean = mapDb.mapCompanyModelToDTOdb(company);
		
		SqlParameterSource params = new BeanPropertySqlParameterSource(cBean);
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		
		temp.update(addCompany, params);
		
	}

	public Optional<Computer> findComputer(int id){		
		
		Optional<Computer> computer = Optional.empty();
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
				
		ComputerBeanDb cBean = temp.query(getOneComputer, params, computerMapper).get(0);
		computer = Optional.ofNullable(map.mapDAOBeanToComputer(cBean));
		
		return(computer);
		
	}
	
	public boolean updateComputer(Computer computer){

		ComputerBeanDb cBean = mapDb.mapComputerModelToDTOdb(computer);
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		SqlParameterSource params = new BeanPropertySqlParameterSource(cBean);
		
		temp.update(updateComputer, params);
		
		return true;
	}
	
	public boolean deleteComputer(int id){

		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		temp.update(deleteComputer, params);
		
		return true;
		
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
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		MapSqlParameterSource params = new MapSqlParameterSource();

		
		for(int i=0; i<buffer.length; i++) {
			
			query+=":id"+i+", ";
			params.addValue("id"+i, Integer.parseInt(buffer[i]));
			
		}
		
		temp.update(query, params);
		
	}
	
	
	/*            Composed Requests             */	
	
	public ArrayList<Computer> listComputer(RequestParameter parameters){
			
		List<ComputerBeanDb> searchResult = new ArrayList<ComputerBeanDb>();
		ArrayList<Computer> computerList = new ArrayList<Computer>();
		
		String query = getComputer+allComputers+searchComputer;
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("searchTerm", "%"+parameters.getSearchTerm()+"%");
		
		searchResult = temp.query(query, params, computerMapper);
		
		computerList = (ArrayList<Computer>) searchResult.stream()
					.map(s -> map.mapDAOBeanToComputer(s))
					.collect(Collectors.toList());
		
		return(computerList);
		
	}		
	
	public ArrayList<Company> listCompany(RequestParameter parameters){
			
		List<CompanyBeanDb> searchResult = new ArrayList<CompanyBeanDb>();
		ArrayList<Company> computerList = new ArrayList<Company>();
		
		String query = getCompany+allCompanies+searchCompany;
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("searchTerm", "%"+parameters.getSearchTerm()+"%");
		
		searchResult = temp.query(query, params, companyMapper);
		
		computerList = (ArrayList<Company>) searchResult.stream()
					.map(s -> map.mapDAOBeanToCompany(s))
					.collect(Collectors.toList());
		
		return(computerList);
	}		
	
	public int countComputer(RequestParameter parameters) {
			
		String query = countComputer+allComputers+searchComputer;
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("searchTerm", "%"+parameters.getSearchTerm()+"%");
		
		int count = temp.queryForObject(query, params, Integer.class);
					
		return(count);
	}			
	
	public int countCompany(RequestParameter parameters) {
			
		String query = countCompany+allCompanies+searchCompany;
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("searchTerm", "%"+parameters.getSearchTerm()+"%");
		
		int count = temp.queryForObject(query, params, Integer.class);
					
		return(count);
	}			
	
	public ArrayList<Computer> pageComputer(Page page){
		
		
		RequestParameterBeanDb parameters = mapDb.mapParametersToDTOdb(page.getParameters());
		List<ComputerBeanDb> searchResult = new ArrayList<ComputerBeanDb>();
		ArrayList<Computer> computerList = new ArrayList<Computer>();
		
		String query = getComputer+allComputers+searchComputer+orderBy+columnComputer[parameters.getChoice()]+order[parameters.getOrder()]+pageModel;
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("searchTerm", "%"+parameters.getSearchTerm()+"%");
		params.addValue("size",page.getTaille());
		params.addValue("start", page.getStart());
		
		searchResult = temp.query(query, params, computerMapper);
		
		computerList = (ArrayList<Computer>) searchResult.stream()
					.map(s -> map.mapDAOBeanToComputer(s))
					.collect(Collectors.toList());
					
		
		return(computerList);

	}		
	
	public ArrayList<Company> pageCompany(Page page){
			
		RequestParameterBeanDb parameters = mapDb.mapParametersToDTOdb(page.getParameters());
		List<CompanyBeanDb> searchResult = new ArrayList<CompanyBeanDb>();
		ArrayList<Company> computerList = new ArrayList<Company>();
		
		String query = getCompany+allCompanies+searchCompany+orderBy+columnCompany[parameters.getChoice()]+order[parameters.getOrder()]+pageModel;
		
		NamedParameterJdbcTemplate temp = new NamedParameterJdbcTemplate(db.getDataSource());
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("searchTerm", "%"+parameters.getSearchTerm()+"%");
		params.addValue("size",page.getTaille());
		params.addValue("start", page.getStart());
		
		searchResult = temp.query(query, params, companyMapper);
		
		computerList = (ArrayList<Company>) searchResult.stream()
					.map(s -> map.mapDAOBeanToCompany(s))
					.collect(Collectors.toList());					
		
		return(computerList);	
	}
	
	/*            Utility             */

	public Database getDb() {
		return db;
	}

	

}
