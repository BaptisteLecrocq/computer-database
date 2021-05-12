package com.excilys.model;

public abstract class Page {
	
	protected int taille;
	protected int start;
	protected int end;
	
	public abstract Page nextPage();
	public abstract Page previousPage();
	
}
