package main.java.com.excilys.cdb.controller;

import java.time.LocalDate;

public class Validation {
	
	public boolean tailleValide(int taille) {
		return(taille>0);
	}
	
	public boolean nameValide(String name) {
		return(name!=null&&name!="");
	}
	
	public boolean startBeforeEndValide(LocalDate start, LocalDate end) {
		return(start.isBefore(end));
	}

}
