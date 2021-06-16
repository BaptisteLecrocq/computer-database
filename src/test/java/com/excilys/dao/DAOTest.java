package com.excilys.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.main;
import com.excilys.cdb.dao.ComputerDAO;
import com.excilys.cdb.dao.Database;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.RequestParameter;

public class DAOTest {
	
	private static Logger logger = LoggerFactory.getLogger(main.class);
	@Autowired
	private static ComputerDAO daotest;
	
	private Database db = daotest.getDb();
    
    @BeforeClass
    public static void testConnection() {
    }
    
    @AfterClass
    public static void close() {
    }
    
    @Test
    public void requestAllComputerNotNull() {
    	
    	ArrayList<Computer> results = daotest.listComputer(new RequestParameter());
    	assertTrue(results.size()>0);
    }
    
    @Test
    public void findComputerNotinH2ButInDB() {
    	String computerDbName = "testupdatearch";
    	Computer results;
    	
		try {
			results = daotest.findComputer(592);
			assertFalse(computerDbName.equals(results.getName()));
			
		} catch (NotFoundException e) {
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
    	
    	Computer computerAdded;
    	
		try {
			
			computerAdded = daotest.findComputer(id);
			assertTrue(testName.equals(computerAdded.getName()));
			
		} catch (NotFoundException e) {
			
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
    	
    	Computer computerUpdated;
    	
		try {
			
			computerUpdated = daotest.findComputer(id);
			assertTrue(testName.equals(computerUpdated.getName()));
			
		} catch (NotFoundException e) {
			
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
    	
    	try {
    		
			Computer computerDeleted = daotest.findComputer(id);
			fail();
			
		} catch (NotFoundException e) {
			
			assertTrue(true);
			
		}
 
    	
    }

}
