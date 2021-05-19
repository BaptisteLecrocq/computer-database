package com.excilys.cdb.model;

import com.excilys.cdb.exception.NotFoundException;

public abstract class PageFactory {
	
	public Page getPage(int start, int taille) throws NotFoundException {
	    return createPage(start, taille);
	  }

	  protected abstract Page createPage(int start, int taille) throws NotFoundException;	
}
