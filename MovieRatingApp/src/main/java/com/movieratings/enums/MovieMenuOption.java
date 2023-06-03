package com.movieratings.enums;

public enum MovieMenuOption {
	RATE_MOVIE, DELETE_RATING, FAVORITE_MOVIE, RETURN_FROM_MOVIE, INVALID_OPTION;

	public static MovieMenuOption fromInt(int option, boolean guestAccount, boolean ratingExists) {
		switch (option) {
		case 1:
			return RATE_MOVIE;
		case 2:
			if (guestAccount) {
				return RETURN_FROM_MOVIE;
			} else {
				if (ratingExists) return DELETE_RATING;
				return FAVORITE_MOVIE;
			}
		case 3:
			if (!guestAccount) {
				if (ratingExists) return FAVORITE_MOVIE;
				return RETURN_FROM_MOVIE;
			}
		case 4:
			if (!guestAccount && ratingExists) return RETURN_FROM_MOVIE;
		default:
			return INVALID_OPTION;
		}
	}
}
