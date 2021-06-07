package com.excilys.cdb.model;

import java.util.ArrayList; 

public class PageCompany extends Page {
	
	private ArrayList<Company> elements;
	
	public PageCompany(int start, int taille, int pageNumber, RequestParameter parameters) {
		 
		this.taille = taille;
		this.start = start;	
		this.pageNumber = pageNumber;
		this.parameters = parameters;
		this.classe = Company.class;
	}
	public PageCompany(int start, int taille) {
		this(start, taille, 0, new RequestParameter());
	}
	public PageCompany(int start, int taille, int pageNumber) {
		this(start, taille, pageNumber, new RequestParameter());		
	}
	
	
	
	public PageCompany nextPage() {
		if (start + taille > count) {
			return (new PageCompany(0, taille, 0, parameters));
		
		} else {
			return (new PageCompany(start + taille, taille, pageNumber + 1, parameters));
		}
		
	}	
	public PageCompany previousPage() {
		if (start - taille < 0) {
			return (new PageCompany(count % taille, taille, count / taille, parameters));
		
		} else {
			return (new PageCompany(start - taille, taille, pageNumber - 1, parameters));
		}
	}
	
	
	
	public ArrayList<Company> getElements() {
		return elements;
	}

	public void setElements(ArrayList<?> elements) {
		this.elements = (ArrayList<Company>) elements;
	}
	
	public String toString() {
		return (elements.toString());
	}
	
}
