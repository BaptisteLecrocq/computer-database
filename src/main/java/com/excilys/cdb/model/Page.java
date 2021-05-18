package main.java.com.excilys.cdb.model;

public abstract class Page {
	
	protected int taille;
	protected int start;
	protected int end;
	protected int pageNumber;
	public static int count;
	
	
	public abstract Page nextPage();
	public abstract Page previousPage();
	
	
	public int getTaille() {
		return taille;
	}
	public void setTaille(int taille) {
		this.taille = taille;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
}
