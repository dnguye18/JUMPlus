package com.dollarsbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dollarsbank.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
