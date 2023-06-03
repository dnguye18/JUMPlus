package com.dollarsbank.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Account implements Serializable {

	private static final long serialVersionUID = 1;
	private static final Integer NEW_ACCOUNT_ID = -1;
	
	public static enum Role {
		ROLE_USER, ROLE_ADMIN
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	@Column(unique = true, nullable = false)
	private String username;
	
	@NotBlank
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToOne(mappedBy = "account", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	@JsonManagedReference
	private Customer customer;
	
	@Column(nullable = false)
	@PositiveOrZero
	private Double balance;
	
	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Transaction> transactions;
	
	
	public Account() {
		
	}
	
	public Account(@NotBlank String username, @NotBlank String password, @NotBlank Double balance) {
		super();
		this.id = NEW_ACCOUNT_ID;
		this.username = username;
		this.password = password;
		this.role = Role.ROLE_USER;
		this.balance = balance;
	}
	
	public Account(Integer id, @NotBlank String username, @NotBlank String password, Role role, @NotBlank Double balance) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.balance = balance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
}
