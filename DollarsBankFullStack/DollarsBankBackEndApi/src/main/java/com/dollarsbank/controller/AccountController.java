package com.dollarsbank.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollarsbank.exception.BadAuthenticationHeaderException;
import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.exception.UsernameTakenException;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.AuthenticationRequest;
import com.dollarsbank.model.AuthenticationResponse;
import com.dollarsbank.service.AccountService;
import com.dollarsbank.service.AuthenticationService;
import com.dollarsbank.service.HeaderService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/account")
public class AccountController {

	@Autowired
	AccountService accountService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	HeaderService headerService;
	
	@PostMapping("/login")
	public ResponseEntity<?> loginAccount(@RequestBody AuthenticationRequest request) throws BadCredentialsException {
		return ResponseEntity.status(201).body(new AuthenticationResponse(authenticationService.authenticate(request)));
	}
	
	@PostMapping("/create-account")
	public ResponseEntity<?> addAccount(@RequestBody Map<String, String> json) throws UsernameTakenException, BadCredentialsException {
		Account account = accountService.createNewAccount(json);
		AuthenticationRequest request = new AuthenticationRequest();
		request.setUsername(account.getUsername());
		request.setPassword(json.get("password"));
		return ResponseEntity.status(201).body(new AuthenticationResponse(authenticationService.authenticate(request)));
	}
	
	@PutMapping("/password")
	public ResponseEntity<?> updatePassword(@RequestHeader HttpHeaders header, @RequestBody Map<String, String> json) throws ResourceNotFoundException, BadAuthenticationHeaderException {
		accountService.updatePassword(headerService.findAccountFromHeader(header), json);
		return ResponseEntity.status(200).body("Password Changed");
	}
}
