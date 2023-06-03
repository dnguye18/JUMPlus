package com.movieratings.dao;

import java.util.List;

import com.movieratings.model.Rating;

public interface RatingDAO {

	public List<Rating> getRatingsByAccountId(int accountId);
	public Rating getRatingByAccountIdAndMovieId(int accountId, int movieId);
	public List<Rating> getGuestRatingsByMovieId(int movieId);
	public List<Rating> getUserRatingsByMovieId(int movieId);
	public Rating addRating(Rating rating);
	public Rating updateRating(Rating rating);

}
