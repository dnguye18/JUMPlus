package com.dollarsbank.exception;

public class AccountDoesNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountDoesNotExistException(String message) {
		super(message);
	}
	
	public AccountDoesNotExistException() {
		super();
	}
}
