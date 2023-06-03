package com.dollarsbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollarsbank.exception.BadAuthenticationHeaderException;
import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.service.HeaderService;
import com.dollarsbank.service.TransactionService;
import com.dollarsbank.util.JsonUtil;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	HeaderService headerService;
	
	
	@GetMapping("/recent")
	public ResponseEntity<?> getRecentTransactions(@RequestHeader HttpHeaders header) throws ResourceNotFoundException, BadAuthenticationHeaderException {
		
		return ResponseEntity.status(200).body(JsonUtil.asJsonString(transactionService.getRecentTransactions(headerService.findAccountFromHeader(header))));
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getUserTransactions(@RequestHeader HttpHeaders header) throws ResourceNotFoundException, BadAuthenticationHeaderException {
		
		return ResponseEntity.status(200).body(JsonUtil.asJsonString(transactionService.getUserTransactions(headerService.findAccountFromHeader(header))));
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllTransactions() {
		return ResponseEntity.status(200).body(JsonUtil.asJsonString(transactionService.getAllTransactions()));
	}
}
