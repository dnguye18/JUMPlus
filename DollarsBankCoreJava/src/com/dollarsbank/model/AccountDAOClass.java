package com.dollarsbank.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dollarsbank.utility.ConnManager;

public class AccountDAOClass implements AccountDAO {
	
	private final static int NO_ACCOUNT_ID = -1;
	private final static int ROW_UPDATED = 1;
	
	private Connection conn = ConnManager.getConnection();

	@Override
	public Account getAccountByUsernameAndPassword(String username, String password) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE username = ? AND password = ?");
			stmt.setString(1, username);
			stmt.setString(2, password);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				String pin = rs.getString(4);
				Double amount = rs.getDouble(5);
				
				return new Account(id, username, password, pin, amount);
			} else {
				return null;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@Override
	public Account getAccountById(int id) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE id = ?");
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				String username = rs.getString(2);
				String password = rs.getString(3);
				String pin = rs.getString(4);
				Double balance = rs.getDouble(5);
				
				return new Account(id, username, password, pin, balance);
			} else {
				return null;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@Override
	public int getAccountIdByUsername(String username) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT id FROM account WHERE username = ?");
			stmt.setString(1, username);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				
				return id;
			} else {
				return NO_ACCOUNT_ID;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return NO_ACCOUNT_ID;
	}

	@Override
	public Account addAccount(Account account) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO "
					+ "account(username, password, pin, balance) "
					+ "VALUES (?, ?, ?, ?)");
			stmt.setString(1, account.getUsername());
			stmt.setString(2, account.getPassword());
			stmt.setString(3, account.getPin());
			stmt.setDouble(4, account.getBalance());
			int result = stmt.executeUpdate();
			if (result == ROW_UPDATED) {
				return account;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	@Override
	public Account updateAccount(Account account) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE account SET balance = ? WHERE id = ?");
			stmt.setDouble(1, account.getBalance());
			stmt.setInt(2, account.getId());
			int result = stmt.executeUpdate();
			if (result == ROW_UPDATED) {
				return account;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
