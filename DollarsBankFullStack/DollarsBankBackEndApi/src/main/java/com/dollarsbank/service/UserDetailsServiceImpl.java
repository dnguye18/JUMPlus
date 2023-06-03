package com.dollarsbank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dollarsbank.model.Account;
import com.dollarsbank.repository.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	AccountRepository accRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> accFound = accRepo.findByUsername(username);
		
		if (accFound.isEmpty()) {
			throw new UsernameNotFoundException(username);
		}
		
		return new UserDetailsImpl(accFound.get());
	}

}
