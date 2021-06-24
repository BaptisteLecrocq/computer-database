package com.excilys.cdb.servlets;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.RequestParameter;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@RestController
@RequestMapping("/rest")
public class RestControllerCentral {
	
	private ComputerService computerService;
	private CompanyService companyService;
	private MapperDTO mapDTO;
	
	private static Logger logger = LoggerFactory.getLogger(RestControllerCentral.class);
	
	public RestControllerCentral(ComputerService computerService, CompanyService companyService, MapperDTO mapDTO) {
		
		this.computerService = computerService;
		this.companyService = companyService;
		this.mapDTO = mapDTO;
		
	}
	
	@GetMapping
	public ArrayList<ComputerBean> findAll(){
		
		ArrayList<Computer> computerList = new ArrayList<Computer>();
		
		try {
			computerList = computerService.listComputer(new RequestParameter());
		
		} catch (NotFoundException e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();			
		}
		
		ArrayList<ComputerBean> beanList = (ArrayList<ComputerBean>) computerList.stream()
											.map(c -> mapDTO.mapComputerToDTO(c))
											.collect(Collectors.toList());		
		return(beanList);	
	}
	
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ComputerBean resource) {
		
		Computer computer = mapDTO.mapDTOToComputer(resource);
		
        computerService.addComputer(computer);
        		
	}
	
	@PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable( "id" ) Long id, @RequestBody ComputerBean resource) {
		
		resource.setId(id.toString());
		Computer computer = mapDTO.mapDTOToComputer(resource);
		
        computerService.updateComputer(computer);
    }
	
	@DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
       
		computerService.deleteComputer(id.intValue());
		
    }

	
	

}
