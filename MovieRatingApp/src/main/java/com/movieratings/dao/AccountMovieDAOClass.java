package com.movieratings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.movieratings.model.AccountMovie;
import com.movieratings.model.Movie;
import com.movieratings.utility.ConnManager;

public class AccountMovieDAOClass implements AccountMovieDAO {

	private final static int ACCOUNT_MOVIE_ADDED = 1;
	private final static int ROW_REMOVED = 1;

	private Connection conn = ConnManager.getConnection();

	@Override
	public List<Movie> getMoviesByAccountId(int accountId) {
		List<Movie> movieList = new ArrayList<>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT movie_id, name FROM account_movie LEFT JOIN movie ON movie_id = movie.id WHERE account_id = ?");
			stmt.setInt(1, accountId);
			ResultSet rs = stmt.executeQuery();

			int movieId;
			String movieName;
			while (rs.next()) {
				movieId = rs.getInt(1);
				movieName = rs.getString(2);
				movieList.add(new Movie(movieId, movieName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieList;
	}

	@Override
	public boolean checkFavoriteMovieExists(AccountMovie accMovie) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account_movie WHERE account_id = ? AND movie_id = ?");
			stmt.setInt(1, accMovie.getAccountId());
			stmt.setInt(2, accMovie.getMovieId());

			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean addFavoriteMovie(AccountMovie accMovie) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO account_movie VALUES(?, ?)");
			stmt.setInt(1, accMovie.getAccountId());
			stmt.setInt(2, accMovie.getMovieId());

			int result = stmt.executeUpdate();

			return result == ACCOUNT_MOVIE_ADDED;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeFavoriteMovie(AccountMovie accMovie) {
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM account_movie WHERE account_id = ? AND movie_id = ?");
			stmt.setInt(1, accMovie.getAccountId());
			stmt.setInt(2, accMovie.getMovieId());

			int result = stmt.executeUpdate();

			return result == ROW_REMOVED;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
