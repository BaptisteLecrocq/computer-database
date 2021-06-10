package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.CompanyBean;
import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.beans.RequestParameterBean;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.RequestParameter;


@Component
public class MapperDTO {
	
	
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public Computer mapDTOToComputer(ComputerBean cBean) {
		
		//Validation
		
		String start = cBean.getIntroduced();
		String end = cBean.getDiscontinued();
		
		LocalDate introduced = null;
		LocalDate discontinued = null;
		
		if (!(start.equals("n") || start.equals("") || start == null)) {
			introduced = LocalDate.parse(start, formatter);
		}
		if (!(end.equals("n") || end.equals("") || end == null)) {
			discontinued = LocalDate.parse(end, formatter);
		}
		
		Computer computer = new Computer
				.ComputerBuilder(cBean.getName())
				.withId(Integer.parseInt(cBean.getId()))
				.withStart(introduced)
				.withEnd(discontinued)
				.withManufacturer(Integer.parseInt(cBean.getCompany()),null)
				.build();
		
		return(computer);
	}
	
	public ComputerBean mapComputerToDTO(Computer c) {
		
		//Validation
		
		Optional<LocalDate> introducedBuffer = Optional.empty();
		Optional<LocalDate> discontinuedBuffer = Optional.empty();
		
		ComputerBean cBean = new ComputerBean();

		cBean.setId(""+c.getId());
		cBean.setName(c.getName());
		cBean.setCompany(c.getManufacturer().getName());
		
		introducedBuffer = Optional.ofNullable(c.getStart());
		if(introducedBuffer.isPresent()) {
			cBean.setIntroduced(introducedBuffer.get().toString());
		}
		
		discontinuedBuffer = Optional.ofNullable(c.getEnd());
		if(discontinuedBuffer.isPresent()) {
			cBean.setDiscontinued(discontinuedBuffer.get().toString());
		}
		
		return(cBean);
	}
	
	public Company mapDTOToCompany(CompanyBean cBean) {
		
		//Validation
		
		Company company = new Company(cBean.getId(),cBean.getName());
		
		return(company);
		
	}
	
	public CompanyBean mapCompanyToDTO(Company company) {
		
		CompanyBean cBean = new CompanyBean();
		
		cBean.setId(company.getId());
		cBean.setName(company.getName());
		
		return(cBean);
		
	}

	public RequestParameter mapParameters(RequestParameterBean pBean) {
		
		RequestParameter parameters = new RequestParameter();
		
		//Validation
		
		parameters.setSearchTerm(pBean.getSearchTerm());
		parameters.setChoice(pBean.getChoice());		
		parameters.setOrder(pBean.getOrder());
		
		
		return(parameters);
	}

}
