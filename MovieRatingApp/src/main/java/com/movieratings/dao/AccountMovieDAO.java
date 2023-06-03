package com.movieratings.dao;

import java.util.List;

import com.movieratings.model.AccountMovie;
import com.movieratings.model.Movie;

public interface AccountMovieDAO {

	public List<Movie> getMoviesByAccountId(int accountId);
	public boolean checkFavoriteMovieExists(AccountMovie accMovie);
	public boolean addFavoriteMovie(AccountMovie accMovie);
	public boolean removeFavoriteMovie(AccountMovie accMovie);

}
