package com.dollarsbank.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dollarsbank.exception.AccountDoesNotExistException;
import com.dollarsbank.exception.InsufficientFundsException;
import com.dollarsbank.exception.InvalidArgumentException;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.AccountDAO;
import com.dollarsbank.model.AccountDAOClass;
import com.dollarsbank.model.ActionType;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.CustomerDAO;
import com.dollarsbank.model.CustomerDAOClass;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.model.TransactionDAO;
import com.dollarsbank.model.TransactionDAOClass;
import com.dollarsbank.view.View;

public class DollarsBankController {
	
	private final static int NEW_TRANSACTION = -1;
	private final static int NEW_ACCOUNT = -1;
	private final static int ACCOUNT_CREATION = 1;
	private final static int LOGIN_ACCOUNT = 2;
	private final static int EXIT_LOGIN_MENU = 3;
	private final static int DEPOSIT_MENU = 1;
	private final static int WITHDRAW_MENU = 2;
	private final static int TRANSFER_MENU = 3;
	private final static int RECENT_TRANSACTION_MENU = 4;
	private final static int CUSTOMER_DETAILS_MENU = 5;
	private final static int EXIT_CUSTOMER_MENU = 6;
	
	View view = new View();
	AccountDAO accountDao = new AccountDAOClass();
	CustomerDAO customerDao = new CustomerDAOClass();
	TransactionDAO transactionDao = new TransactionDAOClass();
	
	
	public void run() {
		Scanner in = new Scanner(System.in);
		loginMenu(in);
		in.close();
	}
	
	private void loginMenu(Scanner in) {
		int choice;
		do {
			view.printLoginMenu();
			
			choice = in.nextInt();
			in.nextLine(); //Clear buffer
			switch (choice) {
			case ACCOUNT_CREATION:
				try {
					customerMenu(in, promptAccountCreation(in));
				} catch (InvalidArgumentException e) {
					view.printInvalidArgumentMessage(e.getMessage());
				}
				break;
			case LOGIN_ACCOUNT:
				try {
					customerMenu(in, attemptLogin(in));
				} catch (AccountDoesNotExistException e) {
					// Instead of making them try again, send it back to menu
					// loop to prevent inescapable loop.
					view.printFailedLoginMessage();
				}
				break;
			case EXIT_LOGIN_MENU:
				continue;
			default:
				view.printInvalidChoiceMessage();
			}
		} while (choice != EXIT_LOGIN_MENU);
	}
	
	private void customerMenu(Scanner in, Account acc) {
		int choice;
		Customer customer = customerDao.getCustomerById(acc.getId());
		do {
			view.printCustomerMenu(customer.getName());
			choice = in.nextInt();
			in.nextLine();
			switch(choice) {
			case DEPOSIT_MENU:
				handleDeposit(in, acc);
				break;
			case WITHDRAW_MENU:
				try {
					handleWithdraw(in, acc);
				} catch (InsufficientFundsException e) {
					view.printInsufficientFundsMessage();
				} catch (InvalidArgumentException e) {
					view.printInvalidArgumentMessage(e.getMessage());
				}
				break;
			case TRANSFER_MENU:
				try {
					handleTransfer(in, acc);
				} catch (InsufficientFundsException e) {
					view.printInsufficientFundsMessage();
				} catch (AccountDoesNotExistException e) {
					view.printInvalidArgumentMessage("Account does not exist.");
				} catch (InvalidArgumentException e) {
					view.printInvalidArgumentMessage(e.getMessage());
				}
				break;
			case RECENT_TRANSACTION_MENU:
				view.printRecentTransactions(transactionDao.getLastFiveTransactionsByAccountId(acc.getId()));
				break;
			case CUSTOMER_DETAILS_MENU:
				view.printCustomerDetails(customer);
				break;
			case EXIT_CUSTOMER_MENU:
				continue;
			default:
				view.printInvalidChoiceMessage();
			}
		} while (choice != EXIT_CUSTOMER_MENU);
	}
	
	private Account promptAccountCreation(Scanner in) throws InvalidArgumentException {
		view.printCreateAccountBanner();
		String customerName = view.promptCustomerName(in);
		String customerAddress = view.promptCustomerAddress(in);
		String customerNumber = view.promptPhoneNumber(in);
		String username = view.promptUsername(in, false);
		validateUsername(username);
		String password = view.promptPassword(in, true);
		validatePassword(password);
		String pin = view.promptPin(in);
		validatePin(pin);
		double amount = view.promptInitialDeposit(in);
		validateInitialDeposit(amount);
		
		accountDao.addAccount(new Account(NEW_ACCOUNT, username, password, pin, amount));
		int id = accountDao.getAccountIdByUsername(username);
		customerDao.addCustomer(new Customer(id, customerName, customerAddress, customerNumber));
		transactionDao.addTransaction(new Transaction(NEW_TRANSACTION, id, ActionType.DEPOSIT, amount, amount, Timestamp.valueOf(LocalDateTime.now())));
		return new Account(id, username, password, pin, amount);
	}
	
	private void validateUsername(String username) throws InvalidArgumentException {
		if (accountDao.getAccountIdByUsername(username) != -1) {
			throw new InvalidArgumentException("Username already exists.");
		}
	}
	
	private void validatePassword(String password) throws InvalidArgumentException {
		Pattern lowercase = Pattern.compile("[a-z]+");
		Pattern uppercase = Pattern.compile("[A-Z]+");
		Pattern specialChar = Pattern.compile("[?|!|@|\"|#|$|%|&|^|*|(|)]+");
		Matcher lowerMatch = lowercase.matcher(password);
		Matcher upperMatch = uppercase.matcher(password);
		Matcher specialMatch = specialChar.matcher(password);
		
		if (!lowerMatch.find() || !upperMatch.find() || !specialMatch.find()) {
			throw new InvalidArgumentException("Password does not meet standards.");
		}
	}
	
	private void validatePin(String pin) throws InvalidArgumentException {
		Pattern digits = Pattern.compile("^[0-9]{4}$");
		Matcher digitsMatcher = digits.matcher(pin);
		
		if (!digitsMatcher.find()) {
			throw new InvalidArgumentException("Invalid pin.");
		}
	}
	
	private void validateInitialDeposit(double amount) throws InvalidArgumentException {
		if (amount < 0) {
			throw new InvalidArgumentException("Initial deposit cannot be negative.");
		}
	}
	
	private Account attemptLogin(Scanner in) throws AccountDoesNotExistException {
		String username = view.promptUsername(in, false);
		String password = view.promptPassword(in, false);
		
		Account acc = accountDao.getAccountByUsernameAndPassword(username, password);
		
		if (acc == null) {
			throw new AccountDoesNotExistException();
		} else {
			return acc;
		}
	}
	
	private void handleDeposit(Scanner in, Account acc) {
		view.printDepositBanner();
		double balance = acc.getBalance();
		double amount = view.promptDeposit(in);
		double newBalance = balance + amount;
		
		acc.setBalance(balance + amount);
		accountDao.updateAccount(acc);
		view.printSuccessMessage(transactionDao.addTransaction(new Transaction(NEW_TRANSACTION, acc.getId(), ActionType.DEPOSIT, amount, newBalance, Timestamp.valueOf(LocalDateTime.now()))));
	}
	
	private void handleWithdraw(Scanner in, Account acc) throws InsufficientFundsException, InvalidArgumentException {
		view.printWithdrawBanner();
		double balance = acc.getBalance();
		double amount = view.promptWithdraw(in, balance);
		
		if (amount < 0) {
			throw new InvalidArgumentException("Cannot have amount less than 0.");
		}
		
		double newBalance = balance - amount;
		if (newBalance < 0) {
			throw new InsufficientFundsException();
		}
		acc.setBalance(newBalance);
		accountDao.updateAccount(acc);
		view.printSuccessMessage(transactionDao.addTransaction(new Transaction(-1, acc.getId(), ActionType.WITHDRAW, amount, newBalance, Timestamp.valueOf(LocalDateTime.now()))));
	}
	
	private void handleTransfer(Scanner in, Account acc) throws InsufficientFundsException, AccountDoesNotExistException, InvalidArgumentException {
		view.printTransferBanner();
		String targetUsername = view.promptUsername(in, true);
		int targetAccountId = accountDao.getAccountIdByUsername(targetUsername);
		
		if (targetAccountId == -1) {
			throw new AccountDoesNotExistException();
		}
		
		double balance = acc.getBalance();
		double amount = view.promptTransfer(in, balance);
		
		if (amount < 0) {
			throw new InvalidArgumentException("Cannot have amount less than 0.");
		}
		
		double newBalance = balance - amount;
		
		if (newBalance < 0) {
			throw new InsufficientFundsException();
		} else {
			acc.setBalance(newBalance);
			accountDao.updateAccount(acc);
		}
		
		Account targetAccount = accountDao.getAccountById(targetAccountId);
		double targetAccountBalance = targetAccount.getBalance() + amount;
		targetAccount.setBalance(targetAccountBalance);
		accountDao.updateAccount(targetAccount);
		
		view.printSuccessMessage(transactionDao.addTransaction(new Transaction(NEW_TRANSACTION, acc.getId(), targetAccountId, ActionType.TRANSFER_OUT, amount, newBalance, Timestamp.valueOf(LocalDateTime.now()))));
		transactionDao.addTransaction(new Transaction(NEW_TRANSACTION, targetAccountId, acc.getId(), ActionType.TRANSFER_IN, amount, targetAccountBalance, Timestamp.valueOf(LocalDateTime.now())));
	}
}
