package com.dollarsbank.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollarsbank.exception.BadAuthenticationHeaderException;
import com.dollarsbank.exception.InsufficientFundsException;
import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.service.HeaderService;
import com.dollarsbank.service.TransactionService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/bank")
public class BankController {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	HeaderService headerService;

	@PostMapping("/deposit")
	public ResponseEntity<?> handleDeposit(@RequestHeader HttpHeaders header, @RequestBody Map<String, String> json) throws ResourceNotFoundException, BadAuthenticationHeaderException {
		return ResponseEntity.status(201)
				.body(transactionService.processDeposit(headerService.findAccountFromHeader(header), json));
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<?> handleWithdraw(@RequestHeader HttpHeaders header, @RequestBody Map<String, String> json) throws ResourceNotFoundException, InsufficientFundsException, BadAuthenticationHeaderException {
		return ResponseEntity.status(201)
				.body(transactionService.processWithdraw(headerService.findAccountFromHeader(header), json));
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<?> handleTransfer(@RequestHeader HttpHeaders header, @RequestBody Map<String, String> json) throws ResourceNotFoundException, InsufficientFundsException, BadAuthenticationHeaderException {
		return ResponseEntity.status(201)
				.body(transactionService.processTransfer(headerService.findAccountFromHeader(header), json));
	}
}
