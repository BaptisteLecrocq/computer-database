package com.excilys.cdb.model;

import com.excilys.cdb.exception.NotFoundException;

public abstract class PageFactory {
	
	public Page getPage(int start, int taille, int numberPage) {
	    return createPage(start, taille, numberPage);
	  }

	  protected abstract Page createPage(int start, int taille, int numberPage);	
}
