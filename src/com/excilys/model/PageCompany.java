package com.excilys.model;

import java.util.ArrayList;

import com.excilys.service.CRUD;

public class PageCompany extends Page{
	
	private ArrayList<Company> elements;
	
	public PageCompany(int start, int taille) {
		
		this.elements = CRUD.getFirst().pageCompanyList(start,taille);
		this.taille = taille;
		this.start = start;
		this.end = start+taille;
		
		
		}
	public PageCompany nextPage() {
		return(new PageCompany(start+taille,taille));
	}
	
	public PageCompany previousPage() {
		return(new PageCompany(start-taille,taille));
	}
	
	
	
	public ArrayList<Company> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Company> elements) {
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
