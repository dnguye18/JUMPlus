package com.dollarsbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dollarsbank.exception.InsufficientFundsException;
import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.model.Account.Role;
import com.dollarsbank.model.Transaction.ActionType;
import com.dollarsbank.repository.AccountRepository;
import com.dollarsbank.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	
	@Mock
	TransactionRepository transactionRepo;
	
	@Mock
	AccountRepository accountRepo;
	
	@InjectMocks
	TransactionService transactionService;
	
	private Account account;
	private Map<String, String> request;
	
	@BeforeEach
	void init() {
		account = new Account(1, "username", "password", Role.ROLE_USER, 1000.);
		account.setCustomer(null);
		List<Transaction> transactionList = new ArrayList<>();
		transactionList.add(new Transaction());
		account.setTransactions(transactionList);
		request = new HashMap<>();
	}

	@Test
	void testGetRecentTransactions() throws Exception {
		when(transactionRepo.findRecentTransactions(account.getId())).thenReturn(account.getTransactions());
		assertEquals(transactionService.getRecentTransactions(account), account.getTransactions());
	}
	
	@Test
	void testGetUserTransactions() throws Exception {
		when(transactionRepo.findRecentTransactions(account.getId())).thenReturn(account.getTransactions());
		assertEquals(transactionService.getRecentTransactions(account), account.getTransactions());
	}
	
	@Test
	void testGetAllTransactions() throws Exception {
		List<Transaction> transactionList = new ArrayList<>();
		transactionList.add(new Transaction());
		transactionList.add(new Transaction());
		
		when(transactionRepo.findAll()).thenReturn(transactionList);
		assertEquals(transactionService.getAllTransactions(), transactionList);
	}
	
	@Test
	void testProcessDeposit() throws Exception {
		Transaction expected = new Transaction(account, ActionType.DEPOSIT, 500., 1500., null);
		request.put("amount", "500");
	
		when(accountRepo.save(account)).thenReturn(account);
		when(transactionRepo.save(Mockito.any(Transaction.class))).thenReturn(Mockito.any(Transaction.class));
		
		Transaction result = transactionService.processDeposit(account, request);
		
		assertEquals(result.getAmount(), expected.getAmount());
		assertEquals(result.getAction(), expected.getAction());
		assertEquals(result.getNewBalance(), expected.getNewBalance());
		assertEquals(account.getBalance(), result.getNewBalance());
		
		verify(accountRepo).save(Mockito.any(Account.class));
		verify(transactionRepo).save(Mockito.any(Transaction.class));
	}
	
	@Test
	void testProcessWithdraw() throws Exception {
		Transaction expected = new Transaction(account, ActionType.WITHDRAW, 500., 500., null);
		request.put("amount", "500");
	
		when(accountRepo.save(account)).thenReturn(account);
		when(transactionRepo.save(Mockito.any(Transaction.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		Transaction result = transactionService.processWithdraw(account, request);
		
		assertEquals(result.getAmount(), expected.getAmount());
		assertEquals(result.getAction(), expected.getAction());
		assertEquals(result.getNewBalance(), expected.getNewBalance());
		assertEquals(account.getBalance(), result.getNewBalance());
		
		verify(accountRepo).save(Mockito.any(Account.class));
		verify(transactionRepo).save(Mockito.any(Transaction.class));
	}
	
	@Test
	void testProcessWithdrawInsufficientFunds() throws Exception {
		request.put("amount", "1500");
		
		assertThrows(InsufficientFundsException.class, () -> {
			transactionService.processWithdraw(account, request);
		});
	}
	
	@Test
	void testProcessTransfer() throws Exception {
		Transaction expected = new Transaction(account, ActionType.TRANSFER_OUT, 500., 500., null);
		Account targetAccount = new Account(2, "username2", "password2", Role.ROLE_USER, 500.);
		request.put("amount", "500");
		request.put("targetAccountUsername", "username2");
		
		when(accountRepo.findByUsername(targetAccount.getUsername())).thenReturn(Optional.of(targetAccount));
		when(accountRepo.save(Mockito.any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		when(transactionRepo.save(Mockito.any(Transaction.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		Transaction result = transactionService.processTransfer(account, request);
		
		assertEquals(result.getAmount(), expected.getAmount());
		assertEquals(result.getAction(), expected.getAction());
		assertEquals(result.getNewBalance(), expected.getNewBalance());
		assertEquals(account.getBalance(), result.getNewBalance());

		verify(accountRepo).findByUsername(Mockito.anyString());
		verify(accountRepo, times(2)).save(Mockito.any(Account.class));
		verify(transactionRepo, times(2)).save(Mockito.any(Transaction.class));
	}
	
	@Test
	void testProcessTransferInsufficientFunds() throws Exception {
		Account targetAccount = new Account(2, "username2", "password2", Role.ROLE_USER, 500.);
		request.put("amount", "1500");
		request.put("targetAccountUsername", "username2");
		
		when(accountRepo.findByUsername(targetAccount.getUsername())).thenReturn(Optional.of(targetAccount));
		
		assertThrows(InsufficientFundsException.class, () -> {
			transactionService.processTransfer(account, request);
		});
	}
	
	@Test
	void testProcessTransferInvalidAccount() throws Exception {
		request.put("amount", "500");
		request.put("targetAccountUsername", "username2");
		
		when(accountRepo.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> {
			transactionService.processTransfer(account, request);
		});
	}
}
