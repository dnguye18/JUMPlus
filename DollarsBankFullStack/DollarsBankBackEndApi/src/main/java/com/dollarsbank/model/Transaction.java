package com.dollarsbank.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Transaction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Integer NEW_TRANSACTION_ID = -1;
	private static final Integer NO_TARGET_ACCOUNT = -1;
	
	public static enum ActionType {
		DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	@JsonBackReference
	private Account account;
	
	@Column(columnDefinition = "INTEGER DEFAULT -1")
	private Integer targetAccountId;
	
	@Enumerated(EnumType.STRING)
	private ActionType action;
	
	@Column(nullable = false)
	@Positive
	private Double amount;
	
	@Column(nullable = false)
	@PositiveOrZero
	private Double newBalance;
	
	@Column(nullable = false)
	private Timestamp timestamp;
	
	public Transaction() {
		
	}
	
	public Transaction(Account account, ActionType action, Double amount, Double newBalance, Timestamp timestamp) {
		this.id = NEW_TRANSACTION_ID;
		this.account = account;
		this.targetAccountId = NO_TARGET_ACCOUNT;
		this.action = action;
		this.amount = amount;
		this.newBalance = newBalance;
		this.timestamp = timestamp;
	}
	
	public Transaction(Account account, Integer targetAccountId, ActionType action, Double amount, Double newBalance, Timestamp timestamp) {
		this.id = NEW_TRANSACTION_ID;
		this.account = account;
		this.targetAccountId = targetAccountId;
		this.action = action;
		this.amount = amount;
		this.newBalance = newBalance;
		this.timestamp = timestamp;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Integer getTargetAccountId() {
		return targetAccountId;
	}

	public void setTargetAccountId(Integer targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

	public ActionType getAction() {
		return action;
	}

	public void setAction(ActionType action) {
		this.action = action;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(Double newBalance) {
		this.newBalance = newBalance;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
