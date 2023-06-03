package com.movieratings.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.movieratings.dao.AccountDAO;
import com.movieratings.dao.AccountDAOClass;
import com.movieratings.dao.AccountMovieDAO;
import com.movieratings.dao.AccountMovieDAOClass;
import com.movieratings.dao.MovieDAO;
import com.movieratings.dao.MovieDAOClass;
import com.movieratings.dao.RatingDAO;
import com.movieratings.dao.RatingDAOClass;
import com.movieratings.enums.AccountRole;
import com.movieratings.enums.AdminMenuOption;
import com.movieratings.enums.InitialMenuOption;
import com.movieratings.enums.MovieMenuOption;
import com.movieratings.enums.UserMenuOption;
import com.movieratings.exception.BadEmailFormatException;
import com.movieratings.model.Account;
import com.movieratings.model.AccountMovie;
import com.movieratings.model.Movie;
import com.movieratings.model.Rating;
import com.movieratings.view.ConsoleView;

public class MovieRatingsController {

	private final static int GUEST_ACCOUNT_ID = -1;

	// Return option from movie select
	private final static int RETURN_FROM_SELECT = 0;
	private final static int CLEAR_MOVIE_SELECT = -1;

	private Account currentAccount = null;
	private ConsoleView view = new ConsoleView();
	private AccountDAO accountDao = new AccountDAOClass();
	private AccountMovieDAO accountMovieDao = new AccountMovieDAOClass();
	private MovieDAO movieDao = new MovieDAOClass();
	private RatingDAO ratingDao = new RatingDAOClass();

	public void run() {
		runInitialMenu();
	}

	private void runInitialMenu() {
		InitialMenuOption choice;
		Movie movieSelection;
		do {
			choice = view.promptInitialMenu();
			switch (choice) {
			case CREATE_ACCOUNT:
				runAccountCreate();
				if (this.currentAccount != null) {
					runUserMenu();
				}
				break;
			case LOGIN_ACCOUNT:
				runAccountLogin();
				if (this.currentAccount != null) {
					if (this.currentAccount.getRole() == AccountRole.USER) {
						runUserMenu();
					} else {
						runAdminMenu();
					}
				} else {
					view.displayErrorMessage("Login credentials are incorrect");
				}
				break;
			case VIEW_MOVIES_AS_GUEST:
				do {
					movieSelection = runMovieSelect(movieDao.getAllMovies());
					if (movieSelection != null) runMovieOptions(movieSelection);
				} while (movieSelection != null);
				break;
			case EXIT_PROGRAM:
				continue;
			default:
				view.displayErrorMessage("Invalid choice for menu");
			}
		} while (!choice.equals(InitialMenuOption.EXIT_PROGRAM));
	}

	private void runUserMenu() {
		UserMenuOption choice;
		Movie movieSelection;
		do {
			choice = view.promptAccountMenu();

			switch(choice) {
			case VIEW_ALL_MOVIES:
				do{
					movieSelection = runMovieSelect(movieDao.getAllMovies());
					if (movieSelection != null) runMovieOptions(movieSelection);
				} while (movieSelection != null);
				break;
			case VIEW_FAVORITE_MOVIES:
				do{
					movieSelection = runMovieSelect(accountMovieDao.getMoviesByAccountId(this.currentAccount.getId()));
					if (movieSelection != null) runMovieOptions(movieSelection);
				} while (movieSelection != null);
				break;
			case VIEW_RATINGS:
				runRatingsDisplay();
				break;
			case LOGOUT_ACCOUNT:
				this.currentAccount = null;
				break;
			default:
				view.displayErrorMessage("Invalid choice for menu");
			}
		} while (!choice.equals(UserMenuOption.LOGOUT_ACCOUNT));
	}

	private void runAdminMenu() {
		AdminMenuOption choice;
		Movie movieSelection;
		do {
			choice = view.promptAdminMenu();

			switch(choice) {
			case ADD_MOVIE:
				runAddMovie();
				break;
			case EDIT_MOVIE:
				movieSelection = runMovieSelect(movieDao.getAllMovies());
				if (movieSelection != null) runMovieEdit(movieSelection);
				break;
			case REMOVE_MOVIE:
				do{
					movieSelection = runMovieSelect(movieDao.getAllMovies());
					if (movieSelection != null) movieDao.deleteMovie(movieSelection);
				} while (movieSelection != null);
				break;
			case LOGOUT_ACCOUNT:
				this.currentAccount = null;
				break;
			default:
				view.displayErrorMessage("Invalid choice for menu");
			}
		} while (!choice.equals(AdminMenuOption.LOGOUT_ACCOUNT));
	}

	private Movie runMovieSelect(List<Movie> movieList) {
		int movieNumber, sectionStartIndex = 0, sectionEndIndex;
		boolean startSection = true, endSection;
		List<Movie> sublist;

		if (movieList.size() > 8) {
			sectionEndIndex = 8;
			endSection = false;
		} else {
			sectionEndIndex = movieList.size();
			endSection = true;
		}

		do {
			sublist = movieList.subList(sectionStartIndex, sectionEndIndex);
			movieNumber = view.promptMovieSelect(sublist, startSection, endSection);
			
			if (movieNumber != RETURN_FROM_SELECT) {
				if (!startSection && movieNumber == sectionEndIndex - sectionStartIndex + 1) {
					sectionEndIndex = sectionStartIndex;
					sectionStartIndex -= 7;
					if (sectionStartIndex == 1) {
						sectionStartIndex = 0;
						startSection = true;
					}
					if (endSection) {
						endSection = false;
					}
					movieNumber = CLEAR_MOVIE_SELECT;
				} else if (!endSection) {
					if (movieNumber == 9) {
						sectionStartIndex = sectionEndIndex;
						if (sectionEndIndex + 8 >= movieList.size()) {
							sectionEndIndex = movieList.size();
							endSection = true;
						} else {
							sectionEndIndex += 7;
						}
						if (startSection) {
							startSection = false;
						}
						movieNumber = CLEAR_MOVIE_SELECT;
					}
				}


				if (movieNumber != RETURN_FROM_SELECT && movieNumber != CLEAR_MOVIE_SELECT) {
					if (movieNumber - 1 >= 0 && movieNumber - 1 < sublist.size()) {
						return sublist.get(movieNumber - 1);
					} else {
						view.displayErrorMessage("Invalid choice for movie select");
					}
				}
			}
		} while (movieNumber != RETURN_FROM_SELECT);
		
		return null;
	}

	private void runMovieOptions(Movie movie) {
		MovieMenuOption choice;
		List<Rating> userRatings;
		double averageUserRating;
		List<Rating> guestRatings;
		double averageGuestRating;
		Rating userRating = null;
		if (this.currentAccount != null) {
			userRating = ratingDao.getRatingByAccountIdAndMovieId(this.currentAccount.getId(), movie.getId());
		}
		do {
			boolean movieFavorite = false;
			if (this.currentAccount != null) {
				movieFavorite = accountMovieDao.checkFavoriteMovieExists(new AccountMovie(this.currentAccount.getId(), movie.getId()));
			}
			userRatings = ratingDao.getUserRatingsByMovieId(movie.getId());
			averageUserRating = calculateAverageRatings(userRatings);
			guestRatings = ratingDao.getGuestRatingsByMovieId(movie.getId());
			averageGuestRating = calculateAverageRatings(guestRatings);
			choice = view.promptMovieOptions(movie, averageGuestRating, averageUserRating, guestRatings.size(),
					userRatings.size(), this.currentAccount == null, userRating, movieFavorite);

			switch(choice) {
			case RATE_MOVIE:
				runMovieRating(movie);
				break;
			case FAVORITE_MOVIE:
				toggleMovieFavorite(movie);
				break;
			case RETURN_FROM_MOVIE:
				continue;
			default:
				view.displayErrorMessage("Invalid choice for menu");
			}

		} while (!choice.equals(MovieMenuOption.RETURN_FROM_MOVIE));
	}
	
	private void runRatingsDisplay() {
		List<Rating> ratingsList = ratingDao.getRatingsByAccountId(this.currentAccount.getId());
		Map<Movie,Rating> movieRatingMap = new HashMap<Movie,Rating>();
		Movie movie;
		for (Rating rating: ratingsList) {
			movie = movieDao.getMovieById(rating.getMovieId());
			movieRatingMap.put(movie, rating);
		}
		view.displayRatings(movieRatingMap);
	}

	private void runAccountCreate() {
		try {
			String email = view.promptEmail();
			validateEmail(email);
			String password = view.promptPassword();

			this.currentAccount = accountDao.addAccount(new Account(email, password));
		} catch (BadEmailFormatException e) {
			e.printStackTrace();
		}
	}

	private void runAccountLogin() {
		String email = view.promptEmail();
		String password = view.promptPassword();

		this.currentAccount = accountDao.getAccountByEmailAndPassword(email, password);
	}

	private void runMovieRating(Movie movie) {
		int rating = view.promptMovieRating(movie);
		if (rating < 0 && rating > 5) {

		} else if (rating != 0) {
			int accountId = (this.currentAccount == null) ? GUEST_ACCOUNT_ID : this.currentAccount.getId();
			ratingDao.addRating(new Rating(accountId, movie.getId(), rating));
		}
	}

	private void toggleMovieFavorite(Movie movie) {
		AccountMovie accMovie = new AccountMovie(this.currentAccount.getId(), movie.getId());
		if (accountMovieDao.checkFavoriteMovieExists(accMovie)) {
			accountMovieDao.removeFavoriteMovie(accMovie);
		} else {
			accountMovieDao.addFavoriteMovie(accMovie);
		}
	}

	private void runAddMovie() {
		String movieName = view.promptMovieName();

		if (movieDao.addMovie(new Movie(movieName))) {
			view.displaySuccessMessage(movieName + " successfully added");
		} else {
			view.displayErrorMessage("Unable to add " + movieName + " to movie list");
		}
	}
	
	private void runMovieEdit(Movie movieToEdit) {
		String newMovieName = view.promptMovieName();
		movieToEdit.setName(newMovieName);
		
		if (movieDao.updateMovie(movieToEdit)) {
			view.displaySuccessMessage("Movie name changed to " + newMovieName);
		} else {
			view.displayErrorMessage("Unable to edit movie name");
		}
	}

	private void validateEmail(String email) throws BadEmailFormatException {
		Pattern emailPattern = Pattern.compile("[A-Za-z0-9]+[@][A-Za-z0-9]+[.][com|net|gov]");
		Matcher emailMatcher = emailPattern.matcher(email);
		
		if (!emailMatcher.find()) {
			throw new BadEmailFormatException();
		}
	}

	private double calculateAverageRatings(List<Rating> ratingsList) {
		double totalRating = 0;

		for (Rating r: ratingsList) {
			totalRating += r.getRating();
		}

		return totalRating/ratingsList.size();
	}

}
