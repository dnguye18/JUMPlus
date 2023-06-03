package com.movieratings.utility;

public class ConsolePrinterUtility {

	private final static String MENU_COLOR = ColorsUtility.BLUE;
	private final static String ERROR_COLOR = ColorsUtility.RED;

	public void printMenuMessage(String msg) {
		System.out.print(MENU_COLOR + msg + ColorsUtility.RESET);
	}

	public void printErrorMessage(String msg) {
		System.out.println(ERROR_COLOR + msg + ColorsUtility.RESET);

	}

	public void print(String msg) {
		System.out.print(msg);
	}
}
