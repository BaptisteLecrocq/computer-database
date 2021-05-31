package com.excilys.cdb.model;

public class PageCompanyFactory extends PageFactory {
	
	@Override
	protected PageCompany createPage(int start, int taille, int numberPage) {
	    return new PageCompany(start, taille, numberPage);
	  }
	
}
