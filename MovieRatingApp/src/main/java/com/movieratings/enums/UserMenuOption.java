package com.movieratings.enums;

public enum UserMenuOption {
	VIEW_ALL_MOVIES, VIEW_FAVORITE_MOVIES, VIEW_RATINGS, LOGOUT_ACCOUNT, INVALID_CHOICE;

	public static UserMenuOption fromInt(int choice) {
		switch (choice) {
		case 1:
			return VIEW_ALL_MOVIES;
		case 2:
			return VIEW_FAVORITE_MOVIES;
		case 3:
			return VIEW_RATINGS;
		case 4:
			return LOGOUT_ACCOUNT;
		default:
			return INVALID_CHOICE;
		}
	}
}
