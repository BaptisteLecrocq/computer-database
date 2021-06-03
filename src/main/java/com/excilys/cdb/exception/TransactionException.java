package com.excilys.cdb.exception;

public class TransactionException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String message;
	
	public TransactionException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return(message);
	}

}
