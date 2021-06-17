package com.excilys.cdb.servlets;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.ScopedProxyMode;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class JSPParameter {
	
	private int pageNumber;
	private int order;
	private int choice;
	private String previousSearch = "";
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
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
	public String getPreviousSearch() {
		return previousSearch;
	}
	public void setPreviousSearch(String previousSearch) {
		this.previousSearch = previousSearch;
	}

}
