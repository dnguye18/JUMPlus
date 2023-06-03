package com.movieratings.dao;

import com.movieratings.model.Account;

public interface AccountDAO {

	public Account getAccountByEmailAndPassword(String email, String password);
	public Account addAccount(Account account);

}
