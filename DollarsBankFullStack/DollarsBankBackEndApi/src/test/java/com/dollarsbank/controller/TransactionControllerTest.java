package com.dollarsbank.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.filter.JwtRequestFilter;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.model.Transaction.ActionType;
import com.dollarsbank.service.HeaderService;
import com.dollarsbank.service.TransactionService;
import com.dollarsbank.service.UserDetailsServiceImpl;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
	
	private static final String STARTING_URI = "http://localhost:8080/api/transaction";
	
	@Autowired
	WebApplicationContext context;
	
	MockMvc mvc;

	@MockBean
	TransactionService transactionService;
	
	@MockBean
	HeaderService headerService;
	
	@MockBean
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@MockBean
	JwtRequestFilter filter;
	
	@InjectMocks
	TransactionController transactionController;
	
	HttpHeaders header;
	Account account;
	List<Transaction> transactionList;
	Transaction t1;
	Transaction t2;
	
	@BeforeEach
	void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context)
									.apply(springSecurity())
									.build();
		

		header = new HttpHeaders();
		account = new Account();
		transactionList = new ArrayList<Transaction>();
		t1 = new Transaction(account, ActionType.DEPOSIT, 500., 7500., Timestamp.valueOf(LocalDateTime.now()));
		t1.setId(213);
		t2 = new Transaction(account, 21, ActionType.TRANSFER_IN, 2500., 10000., Timestamp.valueOf(LocalDateTime.now()));
		t2.setId(4123);
		transactionList.add(t1);
		transactionList.add(t2);
	}
	
	@Test
	@WithMockUser
	void testGetRecentTransactions() throws Exception {
		String uri = STARTING_URI + "/recent";
		
		when(headerService.findAccountFromHeader(header)).thenReturn(account);
		when(transactionService.getRecentTransactions(account)).thenReturn(transactionList);
		
		mvc.perform(get(uri)
				.headers(header)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(transactionList.size()))
			.andExpect(jsonPath("$[0].id").value(t1.getId()))
			.andExpect(jsonPath("$[0].targetAccountId").value(t1.getTargetAccountId()))
			.andExpect(jsonPath("$[0].action").value(t1.getAction().toString()))
			.andExpect(jsonPath("$[0].amount").value(t1.getAmount()))
			.andExpect(jsonPath("$[0].newBalance").value(t1.getNewBalance()))
			.andExpect(jsonPath("$[0].timestamp").value(t1.getTimestamp().getTime()))
			.andExpect(jsonPath("$[1].id").value(t2.getId()))
			.andExpect(jsonPath("$[1].targetAccountId").value(t2.getTargetAccountId()))
			.andExpect(jsonPath("$[1].action").value(t2.getAction().toString()))
			.andExpect(jsonPath("$[1].amount").value(t2.getAmount()))
			.andExpect(jsonPath("$[1].newBalance").value(t2.getNewBalance()))
			.andExpect(jsonPath("$[1].timestamp").value(t2.getTimestamp().getTime()));
			
	}
	
	@Test
	@WithMockUser
	void testGetRecentTransactionsInvalidAccount() throws Exception {
		String uri = STARTING_URI + "/recent";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenThrow(new ResourceNotFoundException("Account not found"));
		
		mvc.perform(get(uri)
				.headers(header)
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser
	void testGetUserTransactions() throws Exception {
		String uri = STARTING_URI + "/";
		
		when(headerService.findAccountFromHeader(header)).thenReturn(account);
		when(transactionService.getUserTransactions(account)).thenReturn(transactionList);
		
		mvc.perform(get(uri)
				.headers(header)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(transactionList.size()))
			.andExpect(jsonPath("$[0].id").value(t1.getId()))
			.andExpect(jsonPath("$[0].targetAccountId").value(t1.getTargetAccountId()))
			.andExpect(jsonPath("$[0].action").value(t1.getAction().toString()))
			.andExpect(jsonPath("$[0].amount").value(t1.getAmount()))
			.andExpect(jsonPath("$[0].newBalance").value(t1.getNewBalance()))
			.andExpect(jsonPath("$[0].timestamp").value(t1.getTimestamp().getTime()))
			.andExpect(jsonPath("$[1].id").value(t2.getId()))
			.andExpect(jsonPath("$[1].targetAccountId").value(t2.getTargetAccountId()))
			.andExpect(jsonPath("$[1].action").value(t2.getAction().toString()))
			.andExpect(jsonPath("$[1].amount").value(t2.getAmount()))
			.andExpect(jsonPath("$[1].newBalance").value(t2.getNewBalance()))
			.andExpect(jsonPath("$[1].timestamp").value(t2.getTimestamp().getTime()));;
	}
	
	@Test
	@WithMockUser
	void testGetUserTransactionsInvalidAccount() throws Exception {
		String uri = STARTING_URI + "/";
		
		when(headerService.findAccountFromHeader(header)).thenThrow(new ResourceNotFoundException("Account not found"));
		
		mvc.perform(get(uri)
				.headers(header)
				.with(csrf()))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(roles= {"ADMIN"})
	void testGetAllTransactionsAsAdmin() throws Exception {
		String uri = STARTING_URI + "/all";
		
		when(transactionService.getAllTransactions()).thenReturn(transactionList);
		
		mvc.perform(get(uri)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(transactionList.size()))
			.andExpect(jsonPath("$[0].id").value(t1.getId()))
			.andExpect(jsonPath("$[0].targetAccountId").value(t1.getTargetAccountId()))
			.andExpect(jsonPath("$[0].action").value(t1.getAction().toString()))
			.andExpect(jsonPath("$[0].amount").value(t1.getAmount()))
			.andExpect(jsonPath("$[0].newBalance").value(t1.getNewBalance()))
			.andExpect(jsonPath("$[0].timestamp").value(t1.getTimestamp().getTime()))
			.andExpect(jsonPath("$[1].id").value(t2.getId()))
			.andExpect(jsonPath("$[1].targetAccountId").value(t2.getTargetAccountId()))
			.andExpect(jsonPath("$[1].action").value(t2.getAction().toString()))
			.andExpect(jsonPath("$[1].amount").value(t2.getAmount()))
			.andExpect(jsonPath("$[1].newBalance").value(t2.getNewBalance()))
			.andExpect(jsonPath("$[1].timestamp").value(t2.getTimestamp().getTime()));;
	}
}
