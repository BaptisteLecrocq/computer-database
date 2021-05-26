package com.excilys.cdb.dao;

import java.io.IOException; 
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
	
	private static Logger logger = LoggerFactory.getLogger(Database.class);
	
	private static Database firstDb = new Database();
	public static Database getInstance() {
		return (firstDb);
	}
		
	private static String url;
	private static String username;
	private static String password;
	private static String driver;
	
	public static String getUrl() {
		return url;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}
	
	public static String getDriver() {
		return driver;
	}

	static {
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream configFile = classLoader.getResourceAsStream("db.properties");
		
		try {
			
			properties.load(configFile);
			url = properties.getProperty("url");
			username = properties.getProperty("username");
			password = properties.getProperty("password");
			driver = properties.getProperty("driverClassName");
			
		} catch (IOException e) {
			logger.debug(e.toString());
		}
	}
	
}
