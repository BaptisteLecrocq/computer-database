package com.excilys.cdb.model;

public class PageComputerFactory extends PageFactory {
	
	@Override
	protected PageComputer createPage(int start, int taille, int numberPage, RequestParameter parameters) {
	    return new PageComputer(start, taille, numberPage, parameters);
	  }
}
