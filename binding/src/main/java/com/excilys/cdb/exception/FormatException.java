package com.excilys.cdb.exception;

public class FormatException extends ValidationException{
	
	private static final long serialVersionUID = 1L;

	public FormatException(String format) {
		super("Wrong "+format+" format");
	}

}
