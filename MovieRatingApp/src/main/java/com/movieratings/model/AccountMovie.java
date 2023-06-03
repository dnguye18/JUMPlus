package com.movieratings.model;

public class AccountMovie {

	private int accountId;
	private int movieId;

	public AccountMovie(int accountId, int movieId) {
		this.accountId = accountId;
		this.movieId = movieId;
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

}
