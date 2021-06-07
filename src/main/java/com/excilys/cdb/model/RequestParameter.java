package com.excilys.cdb.model;

public class RequestParameter {

	private String searchTerm = "";
	private int order = 1;
	private int choice = 1;
	
	public RequestParameter() {}
	
	public RequestParameter(String searchTerm, int order, int choice) {
		
		this.searchTerm = searchTerm;
		this.order = order;
		this.choice = choice;
		
	}
	
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	
}
