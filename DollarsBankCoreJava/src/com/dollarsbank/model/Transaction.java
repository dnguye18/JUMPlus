package com.dollarsbank.model;

import java.sql.Timestamp;

public class Transaction {
	
	private final static int NO_TARGET_ACCOUNT = -1;

	private int id;
	private int accountId;
	private int targetAccountId;
	private ActionType action;
	private double amount;
	private double newBalance;
	private Timestamp timestamp;
	
	public Transaction(int id, int accountId, int targetAccountId, ActionType action, double amount, double newBalance,
			Timestamp timestamp) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
		this.action = action;
		this.amount = amount;
		this.newBalance = newBalance;
		this.timestamp = timestamp;
	}
	public Transaction(int id, int accountId, ActionType action, double amount, double newBalance,
			Timestamp timestamp) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.targetAccountId = NO_TARGET_ACCOUNT;
		this.action = action;
		this.amount = amount;
		this.newBalance = newBalance;
		this.timestamp = timestamp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getTargetAccountId() {
		return targetAccountId;
	}
	public void setTargetAccountId(int targetAccountId) {
		this.targetAccountId = targetAccountId;
	}
	public ActionType getAction() {
		return action;
	}
	public void setAction(ActionType action) {
		this.action = action;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getNewBalance() {
		return newBalance;
	}
	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
