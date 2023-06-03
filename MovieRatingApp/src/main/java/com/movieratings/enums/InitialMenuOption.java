package com.movieratings.enums;

public enum InitialMenuOption {
	CREATE_ACCOUNT, LOGIN_ACCOUNT, VIEW_MOVIES_AS_GUEST, EXIT_PROGRAM, INVALID_CHOICE;

	public static InitialMenuOption fromInt(int choice) {
		switch (choice) {
		case 1:
			return CREATE_ACCOUNT;
		case 2:
			return LOGIN_ACCOUNT;
		case 3:
			return VIEW_MOVIES_AS_GUEST;
		case 4:
			return EXIT_PROGRAM;
		default:
			return INVALID_CHOICE;
		}
	}
}
