package com.dollarsbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.dollarsbank.model.AuthenticationRequest;
import com.dollarsbank.util.JwtUtil;

import jakarta.validation.Valid;

@Service
public class AuthenticationService {

	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	public String authenticate(@Valid AuthenticationRequest request) throws BadCredentialsException {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		
		String jwt = jwtUtil.generateTokens(userDetails);
		
		return jwt;
	}
}
