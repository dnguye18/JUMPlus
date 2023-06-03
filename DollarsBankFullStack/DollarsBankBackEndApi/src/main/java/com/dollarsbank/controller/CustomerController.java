package com.dollarsbank.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollarsbank.exception.BadAuthenticationHeaderException;
import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.service.CustomerService;
import com.dollarsbank.service.HeaderService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
	
	@Autowired
	HeaderService headerService;
	
	@Autowired
	CustomerService customerService;

	@GetMapping("/")
	public ResponseEntity<?> getCustomerDetails(@RequestHeader HttpHeaders header) throws ResourceNotFoundException, BadAuthenticationHeaderException {
		return ResponseEntity.status(200).body(customerService.getCustomerDetails(headerService.findAccountFromHeader(header)));
	}
	
	@PutMapping("/")
	public ResponseEntity<?> updateCustomerDetails(@RequestHeader HttpHeaders header, @RequestBody Map<String, String> json) throws ResourceNotFoundException, BadAuthenticationHeaderException {
		return ResponseEntity.status(200).body(customerService.updateCustomerDetails(headerService.findAccountFromHeader(header), json));
	}
}
