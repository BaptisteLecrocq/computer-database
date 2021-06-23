package com.excilys.cdb.servlets;

import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.beans.RequestParameterBean;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.PageComputerFactory;
import com.excilys.cdb.model.RequestParameter;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.ValidationDTO;

//@WebServlet(name = "Dashboard", urlPatterns = "/app")
@Controller
public class Dashboard {

	private final int pageSize = 12;
	
	private static final long serialVersionUID = 1L;
	private static final String[] columnName = { "column.name", "column.introduced", "column.discontinued", "column.company"};
	

	private ComputerService computerService;
	private CompanyService companyService;
	private MapperDTO map;
	private ValidationDTO valDTO;
	private JSPParameter params;
	
	private final PageComputerFactory computerFactory = new PageComputerFactory();
	private Page page;
	
	public Dashboard(ComputerService computerService, CompanyService companyService, MapperDTO map, ValidationDTO valDTO, JSPParameter params) {
		
		this.computerService = computerService;
		this.companyService = companyService;
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
		
		if(refresh != null && refresh.length() > 0) {
			
			pageNumber = 0;
			search = "";
			order = 0;
			choice = 0;
			
			refresh = null;
		}
		
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
		
		
		RequestParameterBean paramBean = new RequestParameterBean();
		paramBean.setSearchTerm(search);
		paramBean.setOrder(order);
		paramBean.setChoice(choice+1);
		
		RequestParameter parameters = map.mapParameters(paramBean);
		
		page = computerFactory.getPage(pageNumber*pageSize, pageSize, pageNumber, parameters);
		
		ArrayList<Computer> computerList = computerService.pageComputer(page);
		page.setElements(computerList);
		
		ArrayList<ComputerBean> beanList = (ArrayList<ComputerBean>) computerList.stream()
											.map(c -> map.mapComputerToDTO(c))
											.collect(Collectors.toList());
		
		Page.count = computerService.countComputer(parameters);		
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
			computerService.deleteComputerList(selection);
		}
		
		return(dashboardView);
	}
	
}

