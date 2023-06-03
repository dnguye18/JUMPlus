package com.movieratings.model;

import com.movieratings.enums.AccountRole;

public class Account {

	private final static int NEW_ACCOUNT = -1;

	private int id;
	private String email;
	private String password;
	private AccountRole role;

	public Account(String email, String password) {
		this.id = NEW_ACCOUNT;
		this.email = email;
		this.password = password;
		this.role = AccountRole.USER;
	}

	public Account(int id, String email, String password, AccountRole role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AccountRole getRole() {
		return role;
	}

	public void setRole(AccountRole role) {
		this.role = role;
	}

}
