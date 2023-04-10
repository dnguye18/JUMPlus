package com.dollarsbank.utility;

public class ConsolePrinterUtility {

	public void printWithColor(String text, String color) {
		System.out.println(color + text + ColorsUtility.RESET);
	}
	
	public void print(String text) {
		System.out.println(text);
	}
	
	public void setInputColor() {
		System.out.print(ColorsUtility.CYAN);
	}
	
	public void resetColor() {
		System.out.print(ColorsUtility.RESET);
		
	}
}
