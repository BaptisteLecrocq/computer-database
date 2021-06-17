package com.excilys.cdb.model;

public class PageCompanyFactory extends PageFactory {
	
	@Override
	protected PageCompany createPage(int start, int taille, int numberPage, RequestParameter parameters) {
	    return new PageCompany(start, taille, numberPage, parameters);
	  }
	
}
