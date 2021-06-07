package com.excilys.cdb.model;

import java.util.ArrayList;

import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.service.CRUD;

public class PageComputer extends Page {
	
	private ArrayList<Computer> elements;
	
	public PageComputer(int start, int taille, int pageNumber, RequestParameter parameters) {
		
		this.taille = taille;
		this.start = start;
		this.pageNumber = pageNumber;
		this.parameters = parameters;
		this.classe = Computer.class;
		
	}
	public PageComputer(int start, int taille) {
		this(start, taille, 0, new RequestParameter());		
	}	
	public PageComputer(int start, int taille, int pageNumber) {
		this(start, taille, pageNumber, new RequestParameter());		
	}
	
	
	public PageComputer nextPage() {
		if (start + taille > count) {
			return (new PageComputer(0, taille, 0, parameters));
		
		} else {
			return (new PageComputer(start + taille, taille, pageNumber + 1, parameters));
		}
		
	}	
	public PageComputer previousPage() {
		if (start - taille < 0) {
			return (new PageComputer(count - count % taille, taille, count / taille, parameters));
		
		} else {
			return (new PageComputer(start - taille, taille, pageNumber - 1, parameters));
		}
	}
	
	
	public ArrayList<Computer> getElements() {
		return elements;
	}

	public void setElements(ArrayList<?> elements) {
		this.elements = (ArrayList<Computer>) elements;
	}
	
	public String toString() {
		return (elements.toString());
	}
	
	
}
