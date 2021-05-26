package com.excilys.cdb.model;

import com.excilys.cdb.exception.NotFoundException;

public class PageComputerFactory extends PageFactory {
	
	@Override
	protected PageComputer createPage(int start, int taille, int numberPage) throws NotFoundException {
	    return new PageComputer(start, taille, numberPage);
	  }
}
