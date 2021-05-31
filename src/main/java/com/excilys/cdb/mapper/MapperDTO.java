package com.excilys.cdb.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.model.Computer;

public class MapperDTO {
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private static MapperDTO firstMapper = new MapperDTO();
	public static MapperDTO getInstance() {
		return (firstMapper);
	}
	
	private MapperDTO() {};
	
	public Computer mapDTOToComputer(ComputerBean cBean) {
		
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
				.withManufacturer(Integer.parseInt(cBean.getCompany()))
				.build();
		
		return(computer);
	}

}
