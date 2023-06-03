package com.dollarsbank.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UsernameTakenException.class)
	public ResponseEntity<?> handleUsernameTakenException(UsernameTakenException ex, WebRequest request){
		return handleGenericException(ex.getMessage(), request, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
		return handleGenericException("Username and Password mismatch", request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<?> handleInsufficientFundsException(InsufficientFundsException ex, WebRequest request) {
		return handleGenericException(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		return handleGenericException(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadAuthenticationHeaderException.class)
	public ResponseEntity<?> handleBadAuthenticationHeaderException(BadAuthenticationHeaderException ex, WebRequest request){
		return handleGenericException(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<?> handleGenericException(String msg, WebRequest request, HttpStatus status) {
		ErrorDetails ed = new ErrorDetails(new Date(), msg, request.getDescription(false));
		return new ResponseEntity<>(ed, status);
	}
}
