package com.excilys.cdb.model;

import com.excilys.cdb.exception.NotFoundException;

public abstract class Page {
	
	protected int taille;
	protected int start;
	protected int pageNumber;
	public static int count;	
	
	public abstract Page nextPage() throws NotFoundException;
	public abstract Page previousPage() throws NotFoundException;
	
	
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
	
	
}
