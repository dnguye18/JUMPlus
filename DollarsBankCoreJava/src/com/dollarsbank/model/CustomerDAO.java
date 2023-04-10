package com.dollarsbank.model;

public interface CustomerDAO {
	public Customer getCustomerById(int id);
	public Customer addCustomer(Customer customer);
}
