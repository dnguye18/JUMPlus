package com.dollarsbank.controller;

import static com.dollarsbank.util.TestUtility.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.filter.JwtRequestFilter;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.service.CustomerService;
import com.dollarsbank.service.HeaderService;
import com.dollarsbank.service.UserDetailsServiceImpl;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
	
	private static final String URI = "http://localhost:8080/api/customer/";
	
	@Autowired
	WebApplicationContext context;
	
	MockMvc mvc;

	@MockBean
	CustomerService customerService;
	
	@MockBean
	HeaderService headerService;
	
	@MockBean
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@MockBean
	JwtRequestFilter filter;
	
	@InjectMocks
	CustomerController customerController;
	
	@BeforeEach
	void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context)
									.apply(springSecurity())
									.build();
	}
	
	@Test
	@WithMockUser
	void testGetCustomerDetails() throws Exception {
		HttpHeaders header = new HttpHeaders();
		Account account = new Account();
		Customer customer = new Customer("Name", "Address", "PhoneNumber");
		
		when(headerService.findAccountFromHeader(header)).thenReturn(account);
		when(customerService.getCustomerDetails(account)).thenReturn(customer);
		
		mvc.perform(get(URI)
				.headers(header)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(customer.getName()))
			.andExpect(jsonPath("$.address").value(customer.getAddress()))
			.andExpect(jsonPath("$.phoneNumber").value(customer.getPhoneNumber()));
	}
	
	@Test
	@WithMockUser
	void testGetCustomerDetailsInvalidAccount() throws Exception {
		HttpHeaders header = new HttpHeaders();
		String errMsg = "No account found";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenThrow(new ResourceNotFoundException(errMsg));
		
		mvc.perform(get(URI)
				.headers(header)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errMsg));
	}
	
	@Test
	@WithMockUser
	void testUpdateCustomerDetails() throws Exception {
		HttpHeaders header = new HttpHeaders();
		Account account = new Account();
		Map<String, String> request = new HashMap<String, String>();
		Customer customer = new Customer("Name", "Address", "PhoneNumber");
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenReturn(account);
		when(customerService.updateCustomerDetails(Mockito.any(Account.class), Mockito.anyMap())).thenReturn(customer);
		
		mvc.perform(put(URI)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(customer.getName()))
			.andExpect(jsonPath("$.address").value(customer.getAddress()))
			.andExpect(jsonPath("$.phoneNumber").value(customer.getPhoneNumber()));
	}
	
	@Test
	@WithMockUser
	void testUpdateCustomerDetailsInvalidAccount() throws Exception {
		HttpHeaders header = new HttpHeaders();
		Map<String, String> request = new HashMap<String, String>();
		String errMsg = "No account found";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenThrow(new ResourceNotFoundException(errMsg));
		
		mvc.perform(put(URI)
				.headers(header)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errMsg));
	}
}
