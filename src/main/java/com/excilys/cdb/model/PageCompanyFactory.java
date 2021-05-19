package com.excilys.cdb.model;

import com.excilys.cdb.exception.NotFoundException;

public class PageCompanyFactory extends PageFactory {
	
	@Override
	protected PageCompany createPage(int start, int taille) throws NotFoundException {
	    return new PageCompany(start, taille);
	  }
	
}
