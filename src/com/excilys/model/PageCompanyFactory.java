package com.excilys.model;

public class PageCompanyFactory extends PageFactory{
	
	@Override
	protected PageCompany createPage(int start,int taille) {
	    return new PageCompany(start,taille);
	  }
	
}
