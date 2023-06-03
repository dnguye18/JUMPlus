package com.dollarsbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollarsbank.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	@Query(value="SELECT * FROM transaction WHERE account_id = ? ORDER BY id DESC LIMIT 5", nativeQuery = true)
	public List<Transaction> findRecentTransactions(Integer accountId);
	
	public List<Transaction> findAllByAccountId(Integer accountId);
}
