package com.excilys.model;

public class PageComputerFactory extends PageFactory{
	
	@Override
	protected PageComputer createPage(int start,int taille) {
	    return new PageComputer(start,taille);
	  }
}
