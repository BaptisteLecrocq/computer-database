package com.excilys.cdb.servlets;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.beans.CompanyBean;
import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.RequestParameter;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validator.ValidationDTO;


@Controller
@RequestMapping("/add")
public class AddComputer {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AddComputer.class);
	
	private ComputerService computerService;
	private CompanyService companyService;
	private MapperDTO map;
	private ValidationDTO valDTO;
	
	public AddComputer(ComputerService computerService, CompanyService companyService, MapperDTO map, ValidationDTO valDTO) {
		
		this.computerService = computerService;
		this.companyService = companyService;
		this.map = map;
		this.valDTO = valDTO;
		
	}
	
	@GetMapping
	protected ModelAndView doGet() {
		
		ModelAndView addView = new ModelAndView("addComputer");
		
		ArrayList<Company> companylist = new ArrayList<Company>();
		ArrayList<CompanyBean> list = new ArrayList<CompanyBean>();
		
		try {
			companylist = companyService.listCompany(new RequestParameter());
			list = (ArrayList<CompanyBean>) companylist.stream()
					.map(c -> map.mapCompanyToDTO(c))
					.collect(Collectors.toList());
			
		} catch (NotFoundException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
		
		addView.addObject("companyList", list);
		
		addView.addObject("computerDTO",new ComputerBean());
		
		return(addView);
		
	}
	
	@PostMapping
	protected ModelAndView doPost( @RequestParam(required = false) boolean validationFront,
									@ModelAttribute("computerDTO") ComputerBean cbean ) {
		
		
		ModelAndView addView = new ModelAndView("addComputer");
		
		System.out.println("Validation Front : "+validationFront);
		validationFront = true;
		
		if(validationFront) {
			
			System.out.println(cbean);
			ArrayList<String> errors = valDTO.validateComputerBean(cbean);
			
			if(errors.isEmpty()) {
				
				Computer computer = map.mapDTOToComputer(cbean);
				computerService.addComputer(computer);
				
			}
			addView.addObject("errors", errors);
		}
		
		return(addView);

	}
	
}
