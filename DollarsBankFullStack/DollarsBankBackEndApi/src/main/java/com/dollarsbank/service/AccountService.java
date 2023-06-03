package com.dollarsbank.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dollarsbank.exception.UsernameTakenException;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.model.Transaction.ActionType;
import com.dollarsbank.repository.AccountRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Transactional
	public Account createNewAccount(Map<String,String> json) throws UsernameTakenException {
		String username = json.get("username");
		String password = json.get("password");
		Double balance = Double.valueOf(json.get("balance"));
		String name = json.get("name");
		String address = json.get("address");
		String phoneNumber = json.get("phoneNumber");
		
		if (accountRepo.findByUsername(username).isPresent()) {
			throw new UsernameTakenException(username);
		}
		
		Account account = new Account(username, encoder.encode(password), balance);
		Customer customer = new Customer(name, address, phoneNumber);
		List<Transaction> transactionList = new ArrayList<Transaction>();
		if (balance > 0) {
			Transaction transaction = new Transaction(account, ActionType.DEPOSIT, balance, balance, Timestamp.valueOf(LocalDateTime.now()));
			transactionList.add(transaction);
		}
		
		customer.setAccount(account);
		account.setCustomer(customer);
		account.setTransactions(transactionList);
		
		accountRepo.save(account);
		
		return account;
	}
	
	public Account updatePassword(Account acc, Map<String, String> json) {
		acc.setPassword(encoder.encode(json.get("password")));
		accountRepo.save(acc);
		return acc;
	}
	
}
