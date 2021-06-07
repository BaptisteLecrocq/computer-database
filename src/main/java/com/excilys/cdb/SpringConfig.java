package com.excilys.cdb;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan( basePackages = { "com.excilys.cdb.controller", 
									"com.excilys.cdb.service", 
									"com.excilys.cdb.dao", 
									"com.excilys.cdb.mapper", 
									"com.excilys.cdb.dao",
									"com.excilys.cdb.servlets",
									"com.excilys.cdb.ui"} )
//@PropertySource( "chemin vers le fichier de config des propriétés" )
public class SpringConfig {

}
