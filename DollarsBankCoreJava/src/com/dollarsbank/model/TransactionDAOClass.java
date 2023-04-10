package com.dollarsbank.model;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dollarsbank.utility.ConnManager;

public class TransactionDAOClass implements TransactionDAO {
	
	private final static int ROW_UPDATED = 1;

	Connection conn = ConnManager.getConnection();
	
	@Override
	public List<Transaction> getLastFiveTransactionsByAccountId(int accountId) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transaction "
					+ "WHERE account_id = ? ORDER BY id DESC LIMIT 5");
			stmt.setInt(1, accountId);
			ResultSet rs = stmt.executeQuery();
			
			List<Transaction> transactionList = new ArrayList<Transaction>();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int targetAccountId = rs.getInt(3);
				ActionType action = ActionType.valueOf(rs.getString(4));
				Double amount = rs.getDouble(5);
				Double newBalance = rs.getDouble(6);
				Timestamp timestamp = rs.getTimestamp(7);
				
				transactionList.add(new Transaction(id, accountId, targetAccountId, action, amount, newBalance, timestamp));
			}
			Collections.reverse(transactionList);
			return transactionList;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Transaction addTransaction(Transaction transaction) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO "
					+ "transaction(account_id, target_account_id, action, "
					+ "amount, new_balance, timestamp) VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, transaction.getAccountId());
			stmt.setInt(2, transaction.getTargetAccountId());
			stmt.setString(3, transaction.getAction().toString());
			stmt.setDouble(4, transaction.getAmount());
			stmt.setDouble(5, transaction.getNewBalance());
			stmt.setTimestamp(6, transaction.getTimestamp());
			
			int result = stmt.executeUpdate();
			if (result == ROW_UPDATED) {
				return transaction;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
