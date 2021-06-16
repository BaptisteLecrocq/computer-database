package com.excilys.cdb.dao;

import java.io.IOException;  
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class Database {
	
	private static Logger logger = LoggerFactory.getLogger(Database.class);
	
		
	private static String url;
	private static String username;
	private static String password;
	private static String driver;
	
	
	private static HikariConfig config;
	private static HikariDataSource ds;	
	
	
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

	private Database (){
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
		
		config = new HikariConfig();
		config.setDriverClassName(driver);
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);		
		config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource(config);
        
	}
	
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
		//return(DriverManager.getConnection(url,username,password));
	}
	
	public HikariDataSource getDataSource() {
		return ds;
	}
	
	
	public void close() {
		
      if (!ds.isClosed()) {
          ds.close();
      }
      
	}
	
	
}
