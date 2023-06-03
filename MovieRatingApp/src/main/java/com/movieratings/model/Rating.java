package com.movieratings.model;

public class Rating {

	private final static int GUEST_ACCOUNT = -1;

	private int accountId;
	private int movieId;
	private int rating;

	public Rating(int accountId, int movieId, int rating) {
		this.accountId = accountId;
		this.movieId = movieId;
		this.rating = rating;
	}

	public Rating(int movieId, int rating) {
		this.accountId = GUEST_ACCOUNT;
		this.movieId = movieId;
		this.rating = rating;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}


	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
