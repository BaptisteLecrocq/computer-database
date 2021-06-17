package com.excilys.cdb.exception;

public class ConnectionException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private static final String message = "Connection to the database failed";
	
	public String getMessage() {
		return message;
	}
	

}
