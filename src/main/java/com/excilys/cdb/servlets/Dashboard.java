package com.excilys.cdb.servlets;

import java.io.IOException; 
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.beans.RequestParameterBean;
import com.excilys.cdb.controller.ControllerCentral;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.PageComputer;
import com.excilys.cdb.model.PageComputerFactory;
import com.excilys.cdb.model.RequestParameter;
import com.excilys.cdb.service.CRUD;
import com.excilys.cdb.validator.ValidationDTO;

//@WebServlet(name = "Dashboard", urlPatterns = "/app")
@Controller
public class Dashboard {

	private final int pageSize = 12;
	
	private static final long serialVersionUID = 1L;
	private static final String[] columnName = { "Computer name", "Introduced date", "Discontinued date", "Company"};
	

	private CRUD service;
	private MapperDTO map;
	private ValidationDTO valDTO;
	private JSPParameter params;
	
	private final PageComputerFactory computerFactory = new PageComputerFactory();
	private Page page;
	
	public Dashboard( CRUD service, MapperDTO map, ValidationDTO valDTO, JSPParameter params) {
		
		this.service = service;
		this.map = map;
		this.valDTO = valDTO;
		this.params = params;
		
	}	

	@GetMapping("/app")
	public ModelAndView doGet( @RequestParam(required=false) Integer pageNumber,
								@RequestParam(required=false) Integer order,
								@RequestParam(required=false) Integer choice,
								@RequestParam(required=false) String search,
								@RequestParam(required=false) String refresh,
								WebRequest request){
		
		ModelAndView dashboardView = new ModelAndView("dashboard");
		
		
		
		//pageNumber
		
		if( pageNumber == null ) {
			pageNumber = params.getPageNumber();
		
		} else {
			params.setPageNumber(pageNumber);
		}
		dashboardView.addObject("pageNumber", pageNumber);
		
		//searchTerm
		if( search == null ) {
			search = params.getPreviousSearch();

		} else {
			params.setPreviousSearch(search);
		}
		dashboardView.addObject("search", search);
		
		//order
		if( order == null ) {
			order = params.getOrder();
		
		} else {
			params.setOrder(order);
		}
		dashboardView.addObject("order", order);
		
		
		//choice
		if( choice == null ) {
			choice = params.getChoice();
		
		} else {
			params.setChoice(choice);
		}
		dashboardView.addObject("choice", choice);
		
		
		
		if(refresh != null && refresh.length() > 0) {
			System.out.println(refresh);
			
			pageNumber = 0;
			search = "";
			order = 0;
			choice = 0;
			
			refresh = null;
		}

		
		
		RequestParameterBean paramBean = new RequestParameterBean();
		paramBean.setSearchTerm(search);
		paramBean.setOrder(order);
		paramBean.setChoice(choice+1);
		
		RequestParameter parameters = map.mapParameters(paramBean);
		
		page = computerFactory.getPage(pageNumber*pageSize, pageSize, pageNumber, parameters);
		
		ArrayList<Computer> computerList = service.pageComputer(page);
		page.setElements(computerList);
		
		ArrayList<ComputerBean> beanList = (ArrayList<ComputerBean>) computerList.stream()
											.map(c -> map.mapComputerToDTO(c))
											.collect(Collectors.toList());
		
		Page.count = service.countComputer(parameters);		
		int count = page.getCount();	
		
		dashboardView.addObject("params", params);
		dashboardView.addObject("columnName", columnName);
		dashboardView.addObject("count", count);					
		dashboardView.addObject("page", beanList);
		
		return(dashboardView);			
	}
	
	@PostMapping("/app")
	public ModelAndView doPost( @RequestParam(required=false) String selection ) {
		
		ModelAndView dashboardView = new ModelAndView("dashboard");
		
		if( selection != null ) {
			service.deleteComputerList(selection);
		}
		
		return(dashboardView);
	}
	
}

