package com.movieratings.exception;

public class BadEmailFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadEmailFormatException() {
		
	}

	public BadEmailFormatException(String message) {
		super(message);
	}

}
