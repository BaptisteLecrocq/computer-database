package com.excilys.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.main;
import com.excilys.cdb.beans.RequestParameterBean;
import com.excilys.cdb.dao.DAO;
import com.excilys.cdb.dao.Database;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.RequestParameter;

public class DAOTest {
	
	private static Logger logger = LoggerFactory.getLogger(main.class);
	@Autowired
	private static DAO daotest;
	
	private Connection con = daotest.getCon();
	private Database db = daotest.getDb();
    
    @BeforeClass
    public static void testConnection() {
    }
    
    @AfterClass
    public static void close() {
    	daotest.close();
    }
    
    @Test
    public void requestAllComputerNotNull() {
    	
    	ArrayList<Computer> results = daotest.listComputer(new RequestParameter());
    	assertTrue(results.size()>0);
    }
    
    @Test
    public void findComputerNotinH2ButInDB() {
    	String computerDbName = "testupdatearch";
    	Optional<Computer> results = daotest.findComputer(592);
    	
    	if(results.isPresent()) {
    		assertFalse(computerDbName.equals(results.get().getName()));
    	}
    	else {
    		fail();
    	}
    }
    
    @Test
    public void addComputer() {
    	
    	String testName = "testh2";
    	
    	Computer computer = new Computer 
    						.ComputerBuilder(testName)
    						.withManufacturer(5,null)
    						.build();
    	
    	daotest.addComputer(computer);
    	
    	int id = daotest.getLastComputerId();    	
    	Optional<Computer> computerAdded = daotest.findComputer(id);
    	
    	if(computerAdded.isPresent()) {
    		assertTrue(testName.equals(computerAdded.get().getName()));
    	}
    	else {
    		fail();
    	}
    	
    }
    
    @Test
    public void updateComputer() {
    	
    	String testName = "testh2update";
    	
    	Computer computer = new Computer
    						.ComputerBuilder("HasToUpdate")
    						.withManufacturer(5,null)
    						.build();
    	
    	daotest.addComputer(computer);    	
    	int id = daotest.getLastComputerId();
    	
    	Computer update = new Computer
    						.ComputerBuilder(testName)
    						.withId(id)
    						.withManufacturer(5,null)
    						.build();
    	
    	daotest.updateComputer(update);
    	
    	Optional<Computer> computerUpdated = daotest.findComputer(id);
    	
    	if(computerUpdated.isPresent()) {
    		assertTrue(testName.equals(computerUpdated.get().getName()));
    	}
    	else {
    		fail();
    	}    	
    }
    
    @Test
    public void deleteComputer() {
    	
    	String testName = "testh2delete";
    	
    	Computer computer = new Computer
    						.ComputerBuilder(testName)
    						.withManufacturer(5,null)
    						.build();
    	
    	daotest.addComputer(computer);    	
    	int id = daotest.getLastComputerId();
    	
    	daotest.deleteComputer(id);
    	
    	Optional<Computer> computerDeleted = daotest.findComputer(id);
    	assertFalse(computerDeleted.isPresent());
    	
    }

}
