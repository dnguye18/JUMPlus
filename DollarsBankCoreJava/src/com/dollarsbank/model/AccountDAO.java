package com.dollarsbank.model;

public interface AccountDAO {
	public Account getAccountByUsernameAndPassword(String username, String password);
	public Account getAccountById(int id);
	public int getAccountIdByUsername(String username);
	public Account addAccount(Account account);
	public Account updateAccount(Account account);
}
