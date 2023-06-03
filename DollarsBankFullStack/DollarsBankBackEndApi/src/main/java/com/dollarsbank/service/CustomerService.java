package com.dollarsbank.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dollarsbank.exception.ResourceNotFoundException;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository customerRepo;
	
	public Customer getCustomerDetails(Account acc) throws ResourceNotFoundException {
		Optional<Customer> optCustomer = customerRepo.findById(acc.getId());
		if (optCustomer.isEmpty()) {
			throw new ResourceNotFoundException("No customer found for given account.");
		}
		return optCustomer.get();
	}
	
	public Customer updateCustomerDetails(Account acc, Map<String, String> json) {
		Customer customer = acc.getCustomer();
		String name = json.get("name");
		String address = json.get("address");
		String phoneNumber = json.get("phoneNumber");
		
		if (!name.isEmpty()) customer.setName(name);
		if (!address.isEmpty()) customer.setAddress(address);
		if (!phoneNumber.isEmpty()) customer.setPhoneNumber(phoneNumber);
		
		customerRepo.save(customer);
		return customer;
	}
	
}
