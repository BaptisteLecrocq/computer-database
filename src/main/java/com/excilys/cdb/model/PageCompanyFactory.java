package main.java.com.excilys.cdb.model;

public class PageCompanyFactory extends PageFactory{
	
	@Override
	protected PageCompany createPage(int start,int taille) {
	    return new PageCompany(start,taille);
	  }
	
}
