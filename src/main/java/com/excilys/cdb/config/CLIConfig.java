package com.excilys.cdb.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@EnableTransactionManagement
@Configuration
@ComponentScan( basePackages = { "com.excilys.cdb.controller", 
									"com.excilys.cdb.service", 
									"com.excilys.cdb.dao", 
									"com.excilys.cdb.mapper",
									"com.excilys.cdb.ui",
									"com.excilys.cdb.validator"} )
public class CLIConfig {
	
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig("/hikari.properties");
		return new HikariDataSource(config);
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		sessionFactory.setDataSource(dataSource());

		sessionFactory.setPackagesToScan("com.excilys.cdb.beans");
		sessionFactory.setHibernateProperties(hibernateProperties());

		return sessionFactory;
	}

	@Bean
	public PlatformTransactionManager hibernateTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	private final Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		return hibernateProperties;
	}
	

}
