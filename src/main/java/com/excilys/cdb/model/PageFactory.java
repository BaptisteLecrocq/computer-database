package main.java.com.excilys.cdb.model;

public abstract class PageFactory {
	
	public Page getPage(int start, int taille) {
	    return createPage(start,taille);
	  }

	  protected abstract Page createPage(int start,int taille);	
}
