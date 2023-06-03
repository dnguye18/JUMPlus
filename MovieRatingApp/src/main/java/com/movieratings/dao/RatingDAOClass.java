package com.movieratings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movieratings.model.Rating;
import com.movieratings.utility.ConnManager;

public class RatingDAOClass implements RatingDAO {

	private final static int RATING_ADDED = 1;
	private final static int RATING_UPDATED = 1;

	private final static int GUEST_ACCOUNT = -1;

	private final static int ACCOUNT_ID_COLUMN = 1;
	private final static int MOVIE_ID_COLUMN = 2;
	private final static int RATING_COLUMN = 3;

	private Connection conn = ConnManager.getConnection();

	@Override
	public List<Rating> getRatingsByAccountId(int accountId) {
		List<Rating> ratingsList = new ArrayList<>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rating WHERE account_id = ?");
			stmt.setInt(1, accountId);

			ResultSet rs = stmt.executeQuery();

			int movieId, rating;
			while (rs.next()) {
				movieId = rs.getInt(MOVIE_ID_COLUMN);
				rating = rs.getInt(RATING_COLUMN);
				ratingsList.add(new Rating(accountId, movieId, rating));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ratingsList;
	}

	@Override
	public Rating getRatingByAccountIdAndMovieId(int accountId, int movieId) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rating WHERE account_id = ? AND movie_id = ?");
			stmt.setInt(1, accountId);
			stmt.setInt(2, movieId);

			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				int rating = resultSet.getInt(RATING_COLUMN);
				return new Rating(accountId, movieId, rating);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Rating> getGuestRatingsByMovieId(int movieId){
		List<Rating> ratingsList = new ArrayList<>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rating WHERE movie_id = ? AND account_id = -1");
			stmt.setInt(1, movieId);

			ResultSet rs = stmt.executeQuery();
			int rating;
			while (rs.next()) {
				rating = rs.getInt(RATING_COLUMN);
				ratingsList.add(new Rating(GUEST_ACCOUNT, movieId, rating));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ratingsList;
	}

	@Override
	public List<Rating> getUserRatingsByMovieId(int movieId){
		List<Rating> ratingsList = new ArrayList<>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rating WHERE movie_id = ? AND NOT account_id = -1");
			stmt.setInt(1, movieId);

			ResultSet rs = stmt.executeQuery();
			int accountId, rating;
			while (rs.next()) {
				accountId = rs.getInt(ACCOUNT_ID_COLUMN);
				rating = rs.getInt(RATING_COLUMN);
				ratingsList.add(new Rating(accountId, movieId, rating));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return ratingsList;
	}

	@Override
	public Rating addRating(Rating rating) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO rating VALUES(?, ?, ?)");
			stmt.setInt(ACCOUNT_ID_COLUMN, rating.getAccountId());
			stmt.setInt(MOVIE_ID_COLUMN, rating.getMovieId());
			stmt.setInt(RATING_COLUMN, rating.getRating());

			int result = stmt.executeUpdate();
			if (result == RATING_ADDED) {
				return rating;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Rating updateRating(Rating rating) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE rating SET rating = ? WHERE account_id = ? AND movie_id = ?");
			stmt.setInt(1, rating.getRating());
			stmt.setInt(2, rating.getAccountId());
			stmt.setInt(3, rating.getMovieId());

			int result = stmt.executeUpdate();
			if (result == RATING_UPDATED) {
				return rating;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
