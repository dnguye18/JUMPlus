package com.movieratings.view;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.movieratings.enums.AdminMenuOption;
import com.movieratings.enums.InitialMenuOption;
import com.movieratings.enums.MovieMenuOption;
import com.movieratings.enums.UserMenuOption;
import com.movieratings.model.Movie;
import com.movieratings.model.Rating;
import com.movieratings.utility.ConsolePrinterUtility;

public class ConsoleView {

	private Scanner scn = new Scanner(System.in);
	private ConsolePrinterUtility printer = new ConsolePrinterUtility();
	private final static int MAX_CONSOLE_WIDTH = 50;


	public InitialMenuOption promptInitialMenu() {
		String initialMenuPrompt = generateBannerRow()
				+ padMessageLeftAlign("1. Register a new account")
				+ padMessageLeftAlign("2. Login account")
				+ padMessageLeftAlign("3. View movies as guest")
				+ padMessageLeftAlign("4. Exit program")
				+ generateBannerRow();

		return InitialMenuOption.fromInt(promptInt(initialMenuPrompt));
	}

	public UserMenuOption promptAccountMenu() {
		String accountMenuPrompt = generateBannerRow()
				+ padMessageLeftAlign("1. View all movies")
				+ padMessageLeftAlign("2. View favorite movies")
				+ padMessageLeftAlign("3. View your movie ratings")
				+ padMessageLeftAlign("4. Logout account")
				+ generateBannerRow();

		return UserMenuOption.fromInt(promptInt(accountMenuPrompt));
	}

	public AdminMenuOption promptAdminMenu() {
		String adminMenuPrompt = generateBannerRow()
				+ padMessageLeftAlign("1. Add new movie")
				+ padMessageLeftAlign("2. Edit a movie")
				+ padMessageLeftAlign("3. Remove a movie")
				+ padMessageLeftAlign("4. Logout account")
				+ generateBannerRow();

		return AdminMenuOption.fromInt(promptInt(adminMenuPrompt));
	}

	public int promptMovieSelect(List<Movie> movieList, boolean firstSection, boolean finalSection) {
		String movieSelectPrompt = generateBannerRow();

		int optionNumber = 1;
		for (Movie movie: movieList) {
			movieSelectPrompt += padMessageLeftAlign(optionNumber++ + ". " + movie.getName());
		}
		if (!firstSection) {
			movieSelectPrompt += padMessageLeftAlign(optionNumber++ + ". *Prev*");
		}
		if (!finalSection) {
			movieSelectPrompt += padMessageLeftAlign(optionNumber++ + ". *Next*");
		}
		
		movieSelectPrompt += padMessageLeftAlign("0. Return to Menu")
				+ generateBannerRow();
		
		return promptInt(movieSelectPrompt);
	}

	public MovieMenuOption promptMovieOptions(Movie movie, double guestRatingValue, double userRatingValue, int numGuestRatings,
			int numUserRatings, boolean guestAccount, Rating accountRating, boolean movieFavorite) {
		String movieMenuPrompt = generateBannerRow()
				+ padMessageLeftAlign("Movie: " + movie.getName())
				+ generateEmptySpaceRow();
		int optionNumber = 1;

		if (numGuestRatings > 0) {
			movieMenuPrompt += padMessageLeftAlign("Guest Rating: " + String.format("%.2f", guestRatingValue) + " / 5.0")
					+ padMessageLeftAlign("Number of Guest Ratings: " + numGuestRatings)
					+ generateEmptySpaceRow();
		} else {
			movieMenuPrompt += padMessageLeftAlign("Guest Rating: N/A")
					+ generateEmptySpaceRow();
		}

		if (numUserRatings > 0) {
			movieMenuPrompt += padMessageLeftAlign("User Rating: " + String.format("%.2f", userRatingValue) + " / 5.0")
					+ padMessageLeftAlign("Number of User Ratings: " + numUserRatings)
					+ generateEmptySpaceRow();
		} else {
			movieMenuPrompt += padMessageLeftAlign("User Rating: N/A")
					+ generateEmptySpaceRow();
		}

		if (!guestAccount) {
			String accountRatingMessage = "Your Rating: ";
			accountRatingMessage += (accountRating == null) ? "N/A" : accountRating.getRating() + " / 5";
			movieMenuPrompt += padMessageLeftAlign(accountRatingMessage)
					+ generateEmptySpaceRow();
		}


		movieMenuPrompt += (accountRating == null) ? padMessageLeftAlign(optionNumber++ + ". Rate Movie") : padMessageLeftAlign(optionNumber++ + ". Update Rating");

		if (guestAccount) {
			movieMenuPrompt += padMessageLeftAlign(optionNumber + ": Return to Movie Select");
		} else {
			if (accountRating != null) {
				movieMenuPrompt += padMessageLeftAlign(optionNumber++ + ". Delete Rating");
			}
			movieMenuPrompt += movieFavorite ? padMessageLeftAlign(optionNumber++ + ". Unfavorite Movie") : padMessageLeftAlign(optionNumber++ + ". Favorite Movie");
			movieMenuPrompt += padMessageLeftAlign(optionNumber + ": Return to Movie Select");
		}

		movieMenuPrompt += generateEmptySpaceRow()
				+ generateBannerRow();

		return MovieMenuOption.fromInt(promptInt(movieMenuPrompt), guestAccount, (accountRating != null));
	}

	public int promptMovieRating(Movie movie) {
		String ratingPrompt = generateBannerRow()
				+ padMessageLeftAlign("Movie: " + movie.getName())
				+ generateEmptySpaceRow()
				+ padMessageLeftAlign("0. Really Bad")
				+ padMessageLeftAlign("1. Bad")
				+ padMessageLeftAlign("2. Not Good")
				+ padMessageLeftAlign("3. Okay")
				+ padMessageLeftAlign("4. Good")
				+ padMessageLeftAlign("5. Great")
				+ generateEmptySpaceRow()
				+ generateBannerRow();

		return promptInt(ratingPrompt);
	}
	
	public void displayRatings(Map<Movie, Rating> ratingsMap) {
		String ratingsDisplay = generateBannerRow();
		
		Set<Movie> mapKey = ratingsMap.keySet();
		Rating rating;
		for (Movie movie: mapKey) {
			rating = ratingsMap.get(movie);
			ratingsDisplay += padMessageCenterSpaced(movie.getName(), rating.getRating() + "/5");
		}
		
		ratingsDisplay += generateBannerRow();
		printer.printMenuMessage(ratingsDisplay);
	}

	public String promptEmail() {
		String emailPrompt = generateBannerRow()
				+ padMessageCenterAlign("Enter an email")
				+ generateBannerRow();

		return promptString(emailPrompt);
	}

	public String promptPassword() {
		String passwordPrompt = generateBannerRow()
				+ padMessageCenterAlign("Enter a password")
				+ generateBannerRow();

		return promptString(passwordPrompt);
	}

	public String promptMovieName() {
		String moviePrompt = generateBannerRow()
				+ padMessageCenterAlign("Enter a movie name")
				+ generateBannerRow();

		return promptString(moviePrompt);
	}

	public void displayErrorMessage(String msg) {
		String errorMessage = generateBannerRow()
				+ padMessageCenterAlign(msg)
				+ generateBannerRow();

		printer.printErrorMessage(errorMessage);
	}
	
	public void displaySuccessMessage(String msg) {
		String successMessage = generateBannerRow()
				+ padMessageCenterAlign(msg)
				+ generateBannerRow();
		
		printer.printMenuMessage(successMessage);
	}

	private String promptString(String msg) {
		printer.printMenuMessage(msg);
		printer.print(": ");
		return scn.nextLine();
	}

	private int promptInt(String msg) {
		printer.printMenuMessage(msg);
		printer.print(": ");
		int choice = scn.nextInt();
		scn.nextLine();
		return choice;
	}

	private String padMessageLeftAlign(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("|   ");
		sb.append(msg);
		int remainingLength = MAX_CONSOLE_WIDTH - sb.length() - 1;

		for (int i = 0; i < remainingLength; i++) {
			sb.append(" ");
		}

		sb.append("|\n");
		return sb.toString();

	}

	private String padMessageCenterAlign(String msg) {
		StringBuilder sb = new StringBuilder();
		double halfSpaceLength = (MAX_CONSOLE_WIDTH - msg.length() - 2) / 2;
		sb.append("|");
		for (int i = 0; i < Math.floor(halfSpaceLength); i++) {
			sb.append(" ");
		}
		sb.append(msg);
		for (int i = 0; i < Math.ceil(halfSpaceLength); i++) {
			sb.append(" ");
		}
		sb.append("|\n");
		return sb.toString();
	}
	
	private String padMessageCenterSpaced(String leftMsg, String rightMsg) {
		String rightSection = rightMsg + "   |";
		StringBuilder sb = new StringBuilder();
		sb.append("|   ");
		sb.append(leftMsg);
		int middleSpaceLength = MAX_CONSOLE_WIDTH - sb.length() - rightSection.length();
		
		for (int i = 0; i < middleSpaceLength; i++) {
			sb.append(" ");
		}
		
		sb.append("|\n");
		return sb.toString();
	}

	private String generateBannerRow() {
		StringBuilder sb = new StringBuilder();
		sb.append("+");
		for (int i = 0; i < MAX_CONSOLE_WIDTH - 2; i++) {
			sb.append("=");
		}
		sb.append("+\n");
		return sb.toString();
	}

	private String generateEmptySpaceRow() {
		StringBuilder sb = new StringBuilder();
		sb.append("|");
		for (int i = 0; i < MAX_CONSOLE_WIDTH - 2; i++) {
			sb.append(" ");
		}
		sb.append("|\n");
		return sb.toString();
	}
}
