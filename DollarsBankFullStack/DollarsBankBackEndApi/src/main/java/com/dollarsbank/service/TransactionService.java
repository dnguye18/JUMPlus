package com.dollarsbank.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dollarsbank.exception.InsufficientFundsException;
import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.model.Transaction.ActionType;
import com.dollarsbank.repository.AccountRepository;
import com.dollarsbank.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepo;
	
	@Autowired
	AccountRepository accRepo;
	
	public List<Transaction> getRecentTransactions(Account acc) {
		return transactionRepo.findRecentTransactions(acc.getId());
	}
	
	public List<Transaction> getUserTransactions(Account acc) {
		return transactionRepo.findAllByAccountId(acc.getId());
	}
	
	public List<Transaction> getAllTransactions() {
		return transactionRepo.findAll();
	}
	
	@Transactional
	public Transaction processDeposit(Account acc, Map<String, String> json) {
		Double amount = Double.valueOf(json.get("amount"));
		acc.setBalance(acc.getBalance() + amount);
		
		Transaction transaction = new Transaction(acc, ActionType.DEPOSIT, amount, acc.getBalance(), Timestamp.valueOf(LocalDateTime.now()));
		transactionRepo.save(transaction);
		accRepo.save(acc);
		
		return transaction;
	}
	
	@Transactional
	public Transaction processWithdraw(Account acc, Map<String, String> json) throws InsufficientFundsException {
		Double amount = Double.valueOf(json.get("amount"));
		
		if (acc.getBalance() < amount) {
			throw new InsufficientFundsException("Cannot withdraw " + amount + " from current balance amount " + acc.getBalance());
		}
		
		acc.setBalance(acc.getBalance() - amount);
		
		Transaction transaction = new Transaction(acc, ActionType.WITHDRAW, amount, acc.getBalance(), Timestamp.valueOf(LocalDateTime.now()));
		transactionRepo.save(transaction);
		accRepo.save(acc);
		
		return transaction;
	}
	
	@Transactional
	public Transaction processTransfer(Account acc, Map<String, String> json) throws ResourceNotFoundException, InsufficientFundsException {
		Double amount = Double.valueOf(json.get("amount"));
		String targetAccountUsername = json.get("targetAccountUsername");
		Optional<Account> targetAccountOpt = accRepo.findByUsername(targetAccountUsername);
		Account targetAccount;
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		
		if (targetAccountOpt.isEmpty()) {
			throw new ResourceNotFoundException("Cannot find account with username " + targetAccountUsername);
		} else {
			targetAccount = targetAccountOpt.get();
		}
		
		if (acc.getBalance() < amount) {
			throw new InsufficientFundsException("Cannot transfer " + amount + " out from current balance amount " + acc.getBalance());
		}
		
		acc.setBalance(acc.getBalance() - amount);
		targetAccount.setBalance(targetAccount.getBalance() + amount);
		
		Transaction transactionOut = new Transaction(acc, targetAccount.getId(), ActionType.TRANSFER_OUT, amount, acc.getBalance(), timestamp);
		Transaction transactionIn = new Transaction(targetAccount, acc.getId(), ActionType.TRANSFER_IN, amount, targetAccount.getBalance(), timestamp);
		
		transactionRepo.save(transactionOut);
		transactionRepo.save(transactionIn);
		
		accRepo.save(acc);
		accRepo.save(targetAccount);
		
		return transactionOut;
	}
}
