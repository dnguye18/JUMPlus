package com.dollarsbank.view;

import java.util.List;
import java.util.Scanner;

import com.dollarsbank.model.ActionType;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.CustomerDAO;
import com.dollarsbank.model.CustomerDAOClass;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.utility.ColorsUtility;
import com.dollarsbank.utility.ConsolePrinterUtility;

public class View {
	
	private ConsolePrinterUtility printer = new ConsolePrinterUtility();
	
	public void printLoginMenu() {
		printer.printWithColor("+---------------------------+\n"
							+ "| DOLLARSBANK Welcomes You! |\n"
							+ "+---------------------------+"
				, ColorsUtility.BLUE);
		printer.print("1. Create New Account");
		printer.print("2. Login");
		printer.print("3. Exit");
		printer.print("");
		printer.printWithColor("Enter Choice (1, 2, or 3) :", ColorsUtility.GREEN);
	}
	
	public void printCustomerMenu(String name) {
		printer.printWithColor(generateCustomerBanner(name), ColorsUtility.BLUE);
		printer.print("1. Deposit Amount");
		printer.print("2. Withdraw Amount");
		printer.print("3. Funds Transfer");
		printer.print("4. View 5 Recent Transactions");
		printer.print("5. Display Customer Information");
		printer.print("6. Sign Out");
		printer.print("");
		printer.printWithColor("Enter Choice (1, 2, 3, 4, 5, or 6) :", ColorsUtility.GREEN);
	}
	
	public void printCreateAccountBanner() {
		printer.printWithColor("+-------------------------------+\n"
							 + "| Enter Details For New Account |\n"
							 + "+-------------------------------+", ColorsUtility.BLUE);
	}
	
	public void printLoginBanner() {
		printer.printWithColor("+---------------------+\n"
				 			 + "| Enter Login Details |\n"
				 			 + "+---------------------+", ColorsUtility.BLUE);
	}
	
	public void printDepositBanner() {
		printer.printWithColor("+----------------------+\n"
							 + "| Enter Deposit Amount |\n"
							 + "+----------------------+", ColorsUtility.BLUE);
	}
	
	public void printWithdrawBanner() {
		printer.printWithColor("+-------------------------+\n"
	 			 			 + "| Enter Withdrawal Amount |\n"
	 			 			 + "+-------------------------+", ColorsUtility.BLUE);
	}
	
	public void printTransferBanner() {
		printer.printWithColor("+----------------------------+\n"
	 			 			 + "| Enter Transfer Information |\n"
	 			 			 + "+----------------------------+", ColorsUtility.BLUE);
	}
	
	public void printRecentTransactions(List<Transaction> transactions) {
		printer.printWithColor("+------------------------+\n"
							 + "| 5 Recent Transactions: |\n"
							 + "+------------------------+", ColorsUtility.BLUE);
		printer.print("Timestamp\t\tAction\t\tAmount\tNew Balance\n-----------------------------------------------------------");
		String transactionString;
		for (Transaction t: transactions) {
			transactionString = t.getTimestamp() + "\t" + t.getAction() + "\t";
			if (t.getAction().equals(ActionType.DEPOSIT)) {
				transactionString += "\t";
			}
			transactionString += t.getAmount() + "\t" + t.getNewBalance();
			printer.print(transactionString);
		}
	}
	
	public void printSuccessMessage(Transaction transaction) {
		String text = "\nYou have successfully ";
		
		switch(transaction.getAction()) {
		case DEPOSIT:
			text += "deposited " + transaction.getAmount() + " to your account.";
			break;
		case WITHDRAW:
			text += "withdrawn " + transaction.getAmount() + " from your account.";
			break;
		case TRANSFER_OUT:
			CustomerDAO customerDao = new CustomerDAOClass();
			text += "transferred " + transaction.getAmount() + " to " + customerDao.getCustomerById(transaction.getTargetAccountId()).getName() + ".";
			break;
		default:
			break;
		}
		
		text += "\n";
		printer.printWithColor(text, ColorsUtility.GREEN);
	}
	
	public void printCustomerDetails(Customer customer) {
		printer.printWithColor("+----------------------+\n"
							 + "| Customer Information |\n"
							 + "+----------------------+", ColorsUtility.BLUE);
		printer.print("Customer Name: " + customer.getName());
		printer.print("Customer Address: " + customer.getAddress());
		printer.print("Customer Phone Number: " + customer.getPhoneNumber());
	}
	
	public void printFailedLoginMessage() {
		printer.printWithColor("Invalid Credentials.", ColorsUtility.RED);
	}
	
	public void printInvalidChoiceMessage() {
		printer.printWithColor("Invalid Choice. Try again.", ColorsUtility.RED);
	}
	
	public void printInsufficientFundsMessage() {
		printer.printWithColor("Insufficient funds to withdraw.", ColorsUtility.RED);
	}
	
	public void printInvalidArgumentMessage(String message) {
		printer.printWithColor(message, ColorsUtility.RED);	
	}
	
	private String promptString(Scanner in, String text) {
		printer.print(text);
		printer.setInputColor();
		
		String response = in.nextLine();
		
		printer.resetColor();
		return response;
	}
	
	private double promptDouble(Scanner in, String text) {
		printer.print(text);
		printer.setInputColor();
		
		double response = in.nextDouble();
		in.nextLine();
		
		printer.resetColor();
		return response;
	}
	
	public String promptUsername(Scanner in, boolean target) {
		String text = "";
		if (target) {
			text += "Destination ";
		}
		text += "User Id :";
		return promptString(in, text);
	}
	
	public String promptPassword(Scanner in, boolean newPass) {
		String text = "Password :";
		if (newPass) {
			text += " 8 Characters With Lower, Upper & Special";
		}
		return promptString(in, text);
	}
	
	public String promptPin(Scanner in) {
		return promptString(in, "Pin (4 digits):");
	}
	
	public String promptCustomerName(Scanner in) {
		return promptString(in, "Customer Name:");
	}
	
	public String promptCustomerAddress(Scanner in) {
		return promptString(in, "Customer Address:");
	}
	
	public String promptPhoneNumber(Scanner in) {
		return promptString(in, "Customer Contact Number:");
	}
	
	public double promptInitialDeposit(Scanner in) {
		return promptDouble(in, "Initial Deposit Amount");
	}
	
	public double promptDeposit(Scanner in) {
		return promptDouble(in, "Deposit Amount:");
	}
	
	public double promptWithdraw(Scanner in, double balance) {
		printer.print("Current Balance: " + balance);
		return promptDouble(in, "Withdrawal Amount:");
	}
	
	public double promptTransfer(Scanner in, double balance) {
		printer.print("Current Balance: " + balance);
		return promptDouble(in, "Transfer Amount:");
	}
	
	private String generateCustomerBanner(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("+-------------");
		for (int i = 0; i < name.length(); i++) {
			sb.append("-");
		}
		sb.append("+");
		
		return sb.toString() + "\n| WELCOME " + name + "!!! |\n" + sb.toString();
	}
}
