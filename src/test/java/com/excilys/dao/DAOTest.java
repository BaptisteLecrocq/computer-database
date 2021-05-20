package com.excilys.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.main;
import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.dao.Database;

public class DAOTest {
	
	private static Logger logger = LoggerFactory.getLogger(main.class);
	private static DAO daotest = DAO.getInstance();
	
	private Connection con = DAO.getCon();
	private Database db = DAO.getDb();
    
    @BeforeClass
    public static void testConnection() {
    	daotest.open();
    }
    
    @AfterClass
    public static void close() {
    	daotest.stop();
    }
    
    @Test
    public void connectionToH2Database() {
    	Optional<ResultSet> results = daotest.listComputer();
    	assertTrue(results.isPresent());
    }
    
    @Test
    public void requestAllComputerNotNull() {
    	
    	ResultSet results = daotest.listComputer().get();
    	try {
			assertTrue(results.next());
			
		} catch (SQLException e) {
			logger.debug(e.toString());
			fail();
		}
    }
    
    @Test
    public void findComputerNotinH2ButInDB() {
    	ResultSet results = daotest.findComputer(592).get();
    	try {
			assertFalse(results.next());
			
		} catch (SQLException e) {
			logger.debug(e.toString());
			fail();
		}
    }
    
    @Test
    public void addComputer() {
    	
    	String testName = "testh2";
    	
    	daotest.addComputer(testName, Optional.empty(), Optional.empty(), 5);
    	
    	ResultSet results = daotest.getLastComputerId().get();
    	
    	int id = 0;
    	try {
    		results.next();
			id = results.getInt(1);
		} catch (SQLException e) {
			logger.debug(e.toString());
		}
    	
    	ResultSet computerAdded = daotest.findComputer(id).get();
    	try {
			computerAdded.next();
			assertTrue(testName.equals(computerAdded.getString("name")));
		} catch (SQLException e) {
			logger.debug(e.toString());
			fail();
		}
    	
    }
    
    @Test
    public void updateComputer() {
    	
    	String testName = "testh2update";
    	
    	daotest.addComputer("hasToUpdate", Optional.empty(), Optional.empty(), 5);
    	
    	ResultSet results = daotest.getLastComputerId().get();
    	
    	int id = 0;
    	try {
    		results.next();
			id = results.getInt(1);
		} catch (SQLException e) {
			logger.debug(e.toString());
		}
    	
    	daotest.updateComputer(id, testName, Optional.empty(), Optional.empty(), 5);
    	
    	ResultSet computerUpdated = daotest.findComputer(id).get();
    	try {
			computerUpdated.next();
			assertTrue(testName.equals(computerUpdated.getString("name")));
		} catch (SQLException e) {
			logger.debug(e.toString());
			fail();
		}
    	
    }
    
    @Test
    public void deleteComputer() {
    	
    	String testName = "testh2delete";
    	
    	daotest.addComputer(testName, Optional.empty(), Optional.empty(), 5);
    	
    	ResultSet results = daotest.getLastComputerId().get();
    	
    	int id = 0;
    	try {
    		results.next();
			id = results.getInt(1);
		} catch (SQLException e) {
			logger.debug(e.toString());
		}
    	
    	daotest.deleteComputer(id);
    	
    	ResultSet computerDeleted = daotest.findComputer(id).get();
    	try {
			assertFalse(computerDeleted.next());
		} catch (SQLException e) {
			logger.debug(e.toString());
			fail();
		}
    	
    }

}
