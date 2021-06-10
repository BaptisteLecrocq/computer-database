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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.beans.CompanyBean;
import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.controller.ControllerCentral;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.RequestParameter;
import com.excilys.cdb.service.CRUD;
import com.excilys.cdb.validator.ValidationDTO;

//@WebServlet(name = "EditComputer", urlPatterns = "/edit")
@Controller
public class EditComputer {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(EditComputer.class);
	
	private CRUD service;
	private MapperDTO map;
	private ValidationDTO valDTO;
	
	public EditComputer( CRUD service, MapperDTO map, ValidationDTO valDTO) {
		
		this.service = service;
		this.map = map;
		this.valDTO = valDTO;
		
	}
	
	@GetMapping("/edit")
	protected ModelAndView doGet( @RequestParam(required=false) int idComputer ) {
		
		ModelAndView editView = new ModelAndView("editComputer");
		editView.addObject("idComputer", idComputer);		
		
		ArrayList<Company> companylist = new ArrayList<Company>();
		ArrayList<CompanyBean> list = new ArrayList<CompanyBean>();
		
		try {
			companylist = service.listCompany(new RequestParameter());
			list = (ArrayList<CompanyBean>) companylist.stream()
					.map(c -> map.mapCompanyToDTO(c))
					.collect(Collectors.toList());
			
		} catch (NotFoundException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
		
		editView.addObject("companyList", list);
		
		editView.addObject("computerDTO", new ComputerBean());
		
		return(editView);
		
	}
	
	@PostMapping("/edit")
	protected ModelAndView doPost( @RequestParam(required = false) boolean validationFront,
									@ModelAttribute("computerDTO") ComputerBean cbean ) {
		
		
		ModelAndView editView = new ModelAndView("editComputer");
		
		System.out.println("Validation Front : "+validationFront);
		validationFront = true;
		
		if(validationFront) {
			
			System.out.println(cbean);
			ArrayList<String> errors = valDTO.validateComputerBean(cbean);
			
			if(errors.isEmpty()) {
				
				Computer computer = map.mapDTOToComputer(cbean);
				service.addComputer(computer);
				
			}
			editView.addObject("errors", errors);
		}
		
		return(editView);

	}

	
	
}
