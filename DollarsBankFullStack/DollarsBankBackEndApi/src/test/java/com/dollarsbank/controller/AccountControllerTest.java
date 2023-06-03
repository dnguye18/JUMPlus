package com.dollarsbank.controller;

import static com.dollarsbank.util.TestUtility.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.exception.UsernameTakenException;
import com.dollarsbank.filter.JwtRequestFilter;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.AuthenticationRequest;
import com.dollarsbank.service.AccountService;
import com.dollarsbank.service.AuthenticationService;
import com.dollarsbank.service.HeaderService;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
	
	private static final String STARTING_URI = "http://localhost:8080/api/account";
	
	@Autowired
	WebApplicationContext context;
	
	MockMvc mvc;

	@MockBean
	AccountService accountService;
	
	@MockBean
	AuthenticationService authenticationService;
	
	@MockBean
	HeaderService headerService;
	
	@MockBean
	JwtRequestFilter filter;
	
	@InjectMocks
	AccountController accountController;
	
	@BeforeEach
	void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context)
									.apply(springSecurity())
									.build();
	}
	
	@Test
	@WithMockUser(roles= {})
	void testSuccessfulLogin() throws Exception {
		String uri = STARTING_URI + "/login";
		Map<String, String> request = new HashMap<>();
		String token = "token";
		
		when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class))).thenReturn(token);
		
		mvc.perform(post(uri)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.jwt").value(token));
	}
	
	@Test
	@WithMockUser(roles= {})
	void testUnsuccessfulLogin() throws Exception {
		String uri = STARTING_URI + "/login";
		Map<String, String> request = new HashMap<>();
		String errorMsg = "Username and Password mismatch";
		
		when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class)))
		.thenThrow(new BadCredentialsException(errorMsg));
		
		mvc.perform(post(uri)
				.content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errorMsg));
	}
	
	@Test
	@WithMockUser(roles= {})
	void testSuccessfulAccountCreate() throws Exception {
		String uri = STARTING_URI + "/create-account";
		Map<String, String> request = new HashMap<String, String>();
		Account account = new Account();
		String token = "token";
		
		when(accountService.createNewAccount(request))
		.thenReturn(account);
		when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class)))
		.thenReturn(token);
		
		mvc.perform(post(uri).content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.jwt").value(token));
	}
	
	@Test
	@WithMockUser(roles= {})
	void testFailedAccountCreateDuplicateUsername() throws Exception {
		String uri = STARTING_URI + "/create-account";
		Map<String, String> request = new HashMap<String, String>();
		
		when(accountService.createNewAccount(Mockito.anyMap()))
		.thenThrow(new UsernameTakenException());
		
		mvc.perform(post(uri).content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isConflict());
	}
	
	@Test
	@WithMockUser(roles= {"USER"})
	void testUpdatePassword() throws Exception {
		String uri = STARTING_URI + "/password";
		HttpHeaders header = new HttpHeaders();
		Map<String, String> request = new HashMap<String, String>();
		Account account = new Account("username", "password", 0.);
		
		request.put("newPassword", "newPassword");
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class)))
		.thenReturn(account);
		when(accountService.updatePassword(Mockito.any(Account.class), Mockito.anyMap()))
		.thenReturn(account);
		
		mvc.perform(put(uri).headers(header).content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles= {"USER"})
	void testUpdatePasswordInvalidAccount() throws Exception {
		String uri = STARTING_URI + "/password";
		HttpHeaders header = new HttpHeaders();
		Map<String, String> request = new HashMap<String, String>();
		String errorMsg = "No account found.";
		
		when(headerService.findAccountFromHeader(Mockito.any(HttpHeaders.class))).thenThrow(new ResourceNotFoundException(errorMsg));
		
		mvc.perform(put(uri).headers(header).content(asJsonString(request))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.with(csrf()))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(errorMsg));
	}
	
}
