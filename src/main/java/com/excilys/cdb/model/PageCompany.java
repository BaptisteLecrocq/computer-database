package com.excilys.cdb.model;

import java.util.ArrayList;

import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.service.CRUD;

public class PageCompany extends Page {
	
	private ArrayList<Company> elements;
	
	public PageCompany(int start, int taille, int pageNumber) {
		
		this.elements = CRUD.getInstance().pageCompanyList(start, taille);
		this.taille = taille;
		this.start = start;	
		this.pageNumber = pageNumber;
	}
	public PageCompany(int start, int taille) {
		this(start, taille, 0);
	}
	
	
	
	public PageCompany nextPage() {
		if (start + taille > count) {
			return (new PageCompany(0, taille, 0));
		
		} else {
			return (new PageCompany(start + taille, taille, pageNumber + 1));
		}
		
	}	
	public PageCompany previousPage() {
		if (start - taille < 0) {
			return (new PageCompany(count % taille, taille, count / taille));
		
		} else {
			return (new PageCompany(start - taille, taille, pageNumber - 1));
		}
	}
	
	
	
	public ArrayList<Company> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Company> elements) {
		this.elements = elements;
	}
	
	public String toString() {
		return (elements.toString());
	}
	
}
