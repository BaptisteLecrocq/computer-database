package com.excilys.cdb.exception;

public class NameValidationException extends ValidationException {

	private static final long serialVersionUID = 1L;

	public NameValidationException() {
		super("Name can't be null");
	}

}
