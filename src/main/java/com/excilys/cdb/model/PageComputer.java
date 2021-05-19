package com.excilys.cdb.model;

import java.util.ArrayList;

import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.service.CRUD;

public class PageComputer extends Page {
	
	private ArrayList<Computer> elements;
	
	public PageComputer(int start, int taille, int pageNumber) throws NotFoundException {
		

		this.elements = CRUD.getInstance().pageComputerList(start, taille);
		this.taille = taille;
		this.start = start;
		this.pageNumber = pageNumber;
		
	}
	public PageComputer(int start, int taille) throws NotFoundException {
		this(start, taille, 0);		
	}
	
	public PageComputer nextPage() throws NotFoundException {
		if (start + taille > count) {
			return (new PageComputer(0, taille, 0));
		
		} else {
			return (new PageComputer(start + taille, taille, pageNumber + 1));
		}
		
	}	
	public PageComputer previousPage() throws NotFoundException {
		if (start - taille < 0) {
			return (new PageComputer(count - count % taille, taille, count / taille));
		
		} else {
			return (new PageComputer(start - taille, taille, pageNumber - 1));
		}
	}
	
	
	public ArrayList<Computer> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Computer> elements) {
		this.elements = elements;
	}
	
	public String toString() {
		return (elements.toString());
	}
	
	
}
