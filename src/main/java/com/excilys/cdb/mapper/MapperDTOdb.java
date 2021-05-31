package com.excilys.cdb.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.dao.ValidationDAO;
import com.excilys.cdb.exception.ValidationException;
import com.excilys.cdb.model.Computer;

public class MapperDTOdb {
	
	private ValidationDAO valDao = new ValidationDAO();
	private static Logger logger = LoggerFactory.getLogger(MapperDTOdb.class);
	
	private static MapperDTOdb firstMapper = new MapperDTOdb();
	public static MapperDTOdb getInstance() {
		return (firstMapper);
	}
	
	private MapperDTOdb() {};
	
	public ComputerBeanDb mapModelToDTOdb( Computer computer ) {
		
		ComputerBeanDb cBean = new ComputerBeanDb();
		
		try {
			valDao.validateComputerBeanDb(cBean);
			
		} catch (ValidationException e) {
			logger.debug(e.getMessage());
		}
		
		
		cBean.setId(computer.getId());
		cBean.setName(computer.getName());
		cBean.setIntroduced(computer.getStart().toString());
		cBean.setDiscontinued(computer.getEnd().toString());
		cBean.setCompanyId(computer.getManufacturer().getId());
		
		return(cBean);
	}
	
}
