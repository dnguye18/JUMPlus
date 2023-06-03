package com.dollarsbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dollarsbank.exception.UsernameTakenException;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Account.Role;
import com.dollarsbank.model.Transaction.ActionType;
import com.dollarsbank.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
	
	@Mock
	private AccountRepository accountRepo;
	
	@Mock
	PasswordEncoder encoder;
	
	@InjectMocks
	private AccountService accountService;
	
	private Map<String, String> request;
	private Account account;
	
	@BeforeEach
	void init() {
		request = new HashMap<>();
		account = new Account(1, "username", "password", Role.ROLE_USER, 0.);
	}
	
	@Test
	void testCreateNewAccount() throws Exception {
		String username = "username";
		String password = "password";
		String name = "name";
		String address = "address";
		String phoneNumber = "phoneNumber";
		String balance = "500";
		
		request.put("username", username);
		request.put("password", password);
		request.put("name", name);
		request.put("address", address);
		request.put("phoneNumber", phoneNumber);
		request.put("balance", balance);
		
		when(accountRepo.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
		when(accountRepo.save(Mockito.any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		Account result = accountService.createNewAccount(request);
		assertEquals(username, result.getUsername());
		assertEquals(encoder.encode(password), result.getPassword());
		assertEquals(Double.valueOf(balance), result.getBalance());
		assertEquals(name, result.getCustomer().getName());
		assertEquals(address, result.getCustomer().getAddress());
		assertEquals(phoneNumber, result.getCustomer().getPhoneNumber());
		
		assertEquals(1, result.getTransactions().size());
		assertEquals(ActionType.DEPOSIT, result.getTransactions().get(0).getAction());
		assertEquals(Double.valueOf(balance), result.getTransactions().get(0).getAmount());
		assertEquals(Double.valueOf(balance), result.getTransactions().get(0).getNewBalance());
	}
	
	@Test
	void testCreateNewAccountNoStarterBalance() throws Exception {
		String username = "username";
		String password = "password";
		String name = "name";
		String address = "address";
		String phoneNumber = "phoneNumber";
		String balance = "0";
		
		request.put("username", username);
		request.put("password", password);
		request.put("name", name);
		request.put("address", address);
		request.put("phoneNumber", phoneNumber);
		request.put("balance", balance);
		
		when(accountRepo.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
		when(accountRepo.save(Mockito.any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

		Account result = accountService.createNewAccount(request);
		assertEquals(0, result.getTransactions().size());
	}
	
	@Test
	void testCreateNewAccountDuplicateUsername() throws Exception {
		String username = "username";
		String password = "password";
		String name = "name";
		String address = "address";
		String phoneNumber = "phoneNumber";
		String balance = "0";
		
		request.put("username", username);
		request.put("password", password);
		request.put("name", name);
		request.put("address", address);
		request.put("phoneNumber", phoneNumber);
		request.put("balance", balance);
		
		when(accountRepo.findByUsername(account.getUsername())).thenReturn(Optional.of(account));
		
		assertThrows(UsernameTakenException.class, () -> {
			accountService.createNewAccount(request);
		});
		
	}
	
	@Test
	void testUpdatePassword() throws Exception {
		request.put("password", "password2");
		
		when(accountRepo.save(Mockito.any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		Account result = accountService.updatePassword(account, request);
		assertEquals(encoder.encode("password2"), result.getPassword());
	}
}
