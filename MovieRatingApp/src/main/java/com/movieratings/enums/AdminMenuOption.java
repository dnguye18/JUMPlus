package com.movieratings.enums;

public enum AdminMenuOption {
	ADD_MOVIE, REMOVE_MOVIE, LOGOUT_ACCOUNT, EDIT_MOVIE, INVALID_CHOICE;

	public static AdminMenuOption fromInt(int choice) {
		switch (choice) {
		case 1:
			return ADD_MOVIE;
		case 2:
			return EDIT_MOVIE;
		case 3:
			return REMOVE_MOVIE;
		case 4:
			return LOGOUT_ACCOUNT;
		default:
			return INVALID_CHOICE;
		}
	}
}
