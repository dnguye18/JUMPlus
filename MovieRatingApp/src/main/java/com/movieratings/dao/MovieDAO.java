package com.movieratings.dao;

import java.util.List;

import com.movieratings.model.Movie;

public interface MovieDAO {

	public Movie getMovieById(int id);
	public List<Movie> getAllMovies();
	public boolean addMovie(Movie movie);
	public boolean updateMovie(Movie movie);
	public boolean deleteMovie(Movie movie);

}
