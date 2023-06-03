package com.dollarsbank.controller;

import static com.dollarsbank.util.TestUtility.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dollarsbank.exception.InsufficientFundsException;
import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.filter.JwtRequestFilter;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.model.Transaction.ActionType;
import com.dollarsbank.service.HeaderService;
import com.dollarsbank.service.TransactionService;
import com.dollarsbank.service.UserDetailsServiceImpl;

@WebMvcTest(BankController.class)
public class BankControllerTest {
	
	private static final String STARTING_URI = "http://localhost:8080/api/bank";

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
	BankController bankController;
	
	HttpHeaders header;
	Account account;
	Map<String, String> request;
	
	@BeforeEach
	void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context)
									.apply(springSecurity())
									.build();
		
		header = new HttpHeaders();
		account = new Account();
		request = new HashMap<>();
	}
	
	@Test
	@WithMockUser
	void testHandleDeposit() throws Exception {
		String uri = STARTING_URI + "/deposit";
		Transaction transaction = new Transaction(account, ActionType.DEPOSIT, 500., 7500., Timestamp.valueOf(LocalDateTime.now()));
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenReturn(account);
		when(transactionService.processDeposit(Mockito.any(Account.class), Mockito.anyMap())).thenReturn(transaction);
		
		mvc.perform(post(uri)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(transaction.getId()))
			.andExpect(jsonPath("$.targetAccountId").value(transaction.getTargetAccountId()))
			.andExpect(jsonPath("$.action").value(transaction.getAction().toString()))
			.andExpect(jsonPath("$.amount").value(transaction.getAmount()))
			.andExpect(jsonPath("$.newBalance").value(transaction.getNewBalance()));
	}
	
	@Test
	@WithMockUser
	void testHandleDeopsitInvalidAccount() throws Exception {
		String uri = STARTING_URI + "/deposit";
		String errMsg = "No account found";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenThrow(new ResourceNotFoundException(errMsg));
		
		mvc.perform(
				post(uri)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errMsg));
	}
	
	@Test
	@WithMockUser
	void testHandleWithdraw() throws Exception {
		String uri = STARTING_URI + "/withdraw";
		Transaction transaction = new Transaction(account, ActionType.WITHDRAW, 134., 54234., Timestamp.valueOf(LocalDateTime.now()));
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenReturn(account);
		when(transactionService.processWithdraw(Mockito.any(Account.class), Mockito.anyMap())).thenReturn(transaction);
		
		mvc.perform(post(uri)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(transaction.getId()))
			.andExpect(jsonPath("$.targetAccountId").value(transaction.getTargetAccountId()))
			.andExpect(jsonPath("$.action").value(transaction.getAction().toString()))
			.andExpect(jsonPath("$.amount").value(transaction.getAmount()))
			.andExpect(jsonPath("$.newBalance").value(transaction.getNewBalance()));
	}
	
	@Test
	@WithMockUser
	void testHandleWithdrawInvalidAccount() throws Exception {
		String uri = STARTING_URI + "/withdraw";
		String errMsg = "No account found";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenThrow(new ResourceNotFoundException(errMsg));
		
		mvc.perform(post(uri)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errMsg));
	}
	
	@Test
	@WithMockUser
	void testHandleWithdrawWithInsufficientFunds() throws Exception {
		String uri = STARTING_URI + "/withdraw";
		String errMsg = "Not enough funds to withdraw";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenReturn(account);
		when(transactionService.processWithdraw(Mockito.any(Account.class), Mockito.anyMap())).thenThrow(new InsufficientFundsException(errMsg));
		
		mvc.perform(post(uri)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errMsg));
	}
	
	@Test
	@WithMockUser
	void testHandleTransfer() throws Exception {
		String uri = STARTING_URI + "/transfer";
		Transaction transaction = new Transaction(account, 1442, ActionType.TRANSFER_OUT, 14321., 3215123., Timestamp.valueOf(LocalDateTime.now()));
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenReturn(account);
		when(transactionService.processTransfer(Mockito.any(Account.class), Mockito.anyMap())).thenReturn(transaction);
		
		mvc.perform(post(uri)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(transaction.getId()))
			.andExpect(jsonPath("$.targetAccountId").value(transaction.getTargetAccountId()))
			.andExpect(jsonPath("$.action").value(transaction.getAction().toString()))
			.andExpect(jsonPath("$.amount").value(transaction.getAmount()))
			.andExpect(jsonPath("$.newBalance").value(transaction.getNewBalance()));
	}
	
	@Test
	@WithMockUser
	void testHandleTransferInvalidAccount() throws Exception {
		String uri = STARTING_URI + "/transfer";
		String errMsg = "No account found";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenThrow(new ResourceNotFoundException(errMsg));
		
		mvc.perform(post(uri)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errMsg));
	}
	
	@Test
	@WithMockUser
	void testHandleTransferWithInsufficientFunds() throws Exception {
		String uri = STARTING_URI + "/transfer";
		String errMsg = "Not enough funds to transfer";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenReturn(account);
		when(transactionService.processTransfer(Mockito.any(Account.class), Mockito.anyMap())).thenThrow(new InsufficientFundsException(errMsg));
		
		mvc.perform(post(uri)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errMsg));
	}
}
