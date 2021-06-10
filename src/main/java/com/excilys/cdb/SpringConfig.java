package com.excilys.cdb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan( basePackages = { "com.excilys.cdb.controller", 
									"com.excilys.cdb.service", 
									"com.excilys.cdb.dao", 
									"com.excilys.cdb.mapper", 
									"com.excilys.cdb.dao",
									"com.excilys.cdb.dao.JDBCMapper",
									"com.excilys.cdb.servlets",
									"com.excilys.cdb.ui",
									"com.excilys.cdb.validator"} )
//@PropertySource( "chemin vers le fichier de config des propriétés" )
public class SpringConfig implements WebMvcConfigurer{

	@Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
 
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
 
        return viewResolver;
    }
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");	
    }
	
}
