package com.dollarsbank.exception;

public class BadAuthenticationHeaderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BadAuthenticationHeaderException() {
		
	}
	
	public BadAuthenticationHeaderException(String msg) {
		super(msg);
	}

}
