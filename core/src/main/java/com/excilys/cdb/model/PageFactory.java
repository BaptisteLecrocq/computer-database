package com.excilys.cdb.model;

public abstract class PageFactory {
	
	public Page getPage(int start, int taille, int numberPage, RequestParameter parameters) {
	    return createPage(start, taille, numberPage, parameters);
	  }

	  protected abstract Page createPage(int start, int taille, int numberPage, RequestParameter parameters);	
}
