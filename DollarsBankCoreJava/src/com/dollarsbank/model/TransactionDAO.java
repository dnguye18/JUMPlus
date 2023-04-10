package com.dollarsbank.model;

import java.util.List;

public interface TransactionDAO {
	public List<Transaction> getLastFiveTransactionsByAccountId(int accountId);
	public Transaction addTransaction(Transaction transaction);
}
