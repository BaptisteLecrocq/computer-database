package com.excilys.cdb.model;

import java.util.ArrayList;

import com.excilys.cdb.exception.NotFoundException;

public abstract class Page {
	
	protected int taille;
	protected int start;
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
	
	public int getCount() {
		return count;
	}
	
	public ArrayList<?> getElements(){
		return getElements();
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	
}
