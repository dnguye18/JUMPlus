package com.dollarsbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Account.Role;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	
	@Mock
	CustomerRepository customerRepo;
	
	@InjectMocks
	CustomerService customerService;
	
	private Account account;
	private Map<String, String> request;
	
	@BeforeEach
	void init() {
		account = new Account(1, "username1", "password1", Role.ROLE_USER, 1000.);
		account.setCustomer(new Customer("Name1", "Address1", "PhoneNumber1"));
		account.setTransactions(new ArrayList<Transaction>());
		request = new HashMap<>();
	}
	
	@Test
	void testUpdateCustomerDetails() throws Exception {
		String name = "Name2";
		String address = "Address2";
		String phoneNumber = "PhoneNumber2";
		request.put("name", name);
		request.put("address", address);
		request.put("phoneNumber", phoneNumber);
		
		when(customerRepo.save(Mockito.any(Customer.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		Customer result = customerService.updateCustomerDetails(account, request);
		
		assertEquals(name, result.getName());
		assertEquals(address, result.getAddress());
		assertEquals(phoneNumber, result.getPhoneNumber());
	}
	
	@Test
	void testUpdateCustomerDetailsOmittedDetail() throws Exception {
		String name = account.getCustomer().getName();
		String address = "Address2";
		String phoneNumber = "PhoneNumber2";
		request.put("name", "");
		request.put("address", address);
		request.put("phoneNumber", phoneNumber);
		
		when(customerRepo.save(Mockito.any(Customer.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		Customer result = customerService.updateCustomerDetails(account, request);
		
		assertEquals(name, result.getName());
		assertEquals(address, result.getAddress());
		assertEquals(phoneNumber, result.getPhoneNumber());
	}
	
	@Test
	void testUpdateCustomerDetailsMultipleOmitted() throws Exception {
		String name = account.getCustomer().getName();
		String address = "Address2";
		String phoneNumber = account.getCustomer().getPhoneNumber();
		request.put("name", "");
		request.put("address", "Address2");
		request.put("phoneNumber", "");
		
		when(customerRepo.save(Mockito.any(Customer.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		Customer result = customerService.updateCustomerDetails(account, request);
		
		assertEquals(name, result.getName());
		assertEquals(address, result.getAddress());
		assertEquals(phoneNumber, result.getPhoneNumber());
	}

}
