package main.java.com.excilys.cdb.model;

import java.util.ArrayList;

import main.java.com.excilys.cdb.service.CRUD;

public class PageComputer extends Page{
	
	private ArrayList<Computer> elements;
	
	public PageComputer(int start, int taille, int pageNumber) {
		
		this.elements = CRUD.getInstance().pageComputerList(start,taille);
		this.taille = taille;
		this.start = start;
		this.pageNumber = pageNumber;
		
	}
	public PageComputer(int start, int taille) {
		
		this.elements = CRUD.getInstance().pageComputerList(start,taille);
		this.taille = taille;
		this.start = start;
		this.pageNumber = 0;
		
	}
	
	public PageComputer nextPage() {
		if(start+taille > count) {
			return(new PageComputer(0,taille,0));
		}
		else {
			return(new PageComputer(start+taille,taille,pageNumber+1));
		}
		
	}	
	public PageComputer previousPage() {
		if(start-taille < 0) {
			return(new PageComputer(count-count%taille,taille,count/taille));
		}
		else {
			return(new PageComputer(start-taille,taille,pageNumber-1));
		}
	}
	
	
	public ArrayList<Computer> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Computer> elements) {
		this.elements = elements;
	}

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
	
	public String toString() {
		return(elements.toString());
	}
	
}
