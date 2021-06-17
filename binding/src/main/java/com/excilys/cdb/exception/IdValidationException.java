package com.excilys.cdb.exception;

public class IdValidationException extends ValidationException{

	private static final long serialVersionUID = 1L;

	public IdValidationException() {
		super("Computer Id doit être un entier positif");
	}

}
