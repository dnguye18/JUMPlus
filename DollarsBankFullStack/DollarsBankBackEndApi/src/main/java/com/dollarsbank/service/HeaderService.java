package com.dollarsbank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.dollarsbank.exception.BadAuthenticationHeaderException;
import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.model.Account;
import com.dollarsbank.repository.AccountRepository;
import com.dollarsbank.util.JwtUtil;

@Service
public class HeaderService {
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	JwtUtil jwtUtil;
	
	public Account findAccountFromHeader(HttpHeaders header) throws ResourceNotFoundException, BadAuthenticationHeaderException {
		String username = jwtUtil.extractUsername(jwtUtil.extractToken(header.getFirst(HttpHeaders.AUTHORIZATION)));
		Optional<Account> optAcc = accountRepo.findByUsername(username);
		
		if (optAcc.isEmpty()) {
			throw new ResourceNotFoundException("Cannot find account with username " + username);
		} else {
			return optAcc.get();
		}
	}
}
