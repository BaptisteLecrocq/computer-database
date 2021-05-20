package com.excilys;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Unit test for simple App.
 */

public class AppTest{
   
	static Connection db;
	private static Logger logger = LoggerFactory.getLogger(AppTest.class);
    
    @BeforeClass
    public static void testConnection() {
    	// URL de la source de donnée String 
		String url = "jdbc:h2:tcp://localhost/~/test";

		try {
			db = DriverManager.getConnection(url, "sa", "");
		
		} catch (SQLException e) {
			logger.error(e.toString());
		}
    }
    
    @AfterClass
    public static void close() {
    	try {
			db.close();
		} catch (SQLException e) {
			logger.debug(e.toString());
		}
    }
    
    @Test
    public void shouldAnswerWithTrue() {
    	String query = "SELECT id,name,introduced,discontinued,company_id FROM computer;";
    	ResultSet results = null;
    	try {
			Statement stmt = db.createStatement();
			results = stmt.executeQuery(query);
			assertTrue(results.next());
			
		} catch (SQLException e) {
			logger.debug(e.toString());
			assertTrue(false);
		}
    }
}
