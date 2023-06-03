package com.movieratings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.movieratings.model.Movie;
import com.movieratings.utility.ConnManager;

public class MovieDAOClass implements MovieDAO {

	private final static int MOVIE_INSERTED = 1;
	private final static int MOVIE_UPDATED = 1;
	private final static int MOVIE_DELETED = 1;

	private Connection conn = ConnManager.getConnection();

	@Override
	public Movie getMovieById(int id) {
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("SELECT * FROM movie WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String name = rs.getString(2);
				return new Movie(id, name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Movie> getAllMovies() {
		List<Movie> movieList = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM movie");
			int id;
			String name;
			while (rs.next()) {
				id = rs.getInt(1);
				name = rs.getString(2);
				movieList.add(new Movie(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movieList;
	}

	@Override
	public boolean addMovie(Movie movie) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO movie(name) VALUES(?)");
			stmt.setString(1, movie.getName());
			int result = stmt.executeUpdate();

			return result == MOVIE_INSERTED;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean updateMovie(Movie movie) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE movie SET name = ? WHERE id = ?");
			stmt.setString(1, movie.getName());
			stmt.setInt(2, movie.getId());
			int result = stmt.executeUpdate();
			
			return result == MOVIE_UPDATED;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean deleteMovie(Movie movie) {
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM movie WHERE id = ?");
			stmt.setInt(1, movie.getId());
			int result = stmt.executeUpdate();
			
			return result == MOVIE_DELETED;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
