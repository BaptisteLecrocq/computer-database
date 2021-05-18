package main.java.com.excilys.cdb.model;

public class PageComputerFactory extends PageFactory{
	
	@Override
	protected PageComputer createPage(int start,int taille) {
	    return new PageComputer(start,taille);
	  }
}
