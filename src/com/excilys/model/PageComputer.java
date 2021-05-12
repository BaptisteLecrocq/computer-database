package com.excilys.model;

import java.util.ArrayList;

import com.excilys.service.CRUD;

public class PageComputer extends Page{
	
	private ArrayList<Computer> elements;
	
	public PageComputer(int start, int taille) {
		
		this.elements = CRUD.getFirst().pageComputerList(start,taille);
		this.taille = taille;
		this.start = start;
		this.end = start+taille;
		
		}
	public PageComputer nextPage() {
		return(new PageComputer(start+taille,taille));
	}
	
	public PageComputer previousPage() {
		return(new PageComputer(start-taille,taille));
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
