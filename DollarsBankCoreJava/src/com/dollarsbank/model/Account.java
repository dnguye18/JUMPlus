package com.dollarsbank.model;

public class Account {
	
	private int id;
	private String username;
	private String password;
	private String pin;
	private Double balance;
	
	public Account(int id, String username, String password, String pin, Double balance) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.pin = pin;
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public Double getBalance() {
		return balance;
	}
	
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}
