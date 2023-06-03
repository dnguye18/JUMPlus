package com.movieratings.model;

public class Movie {

	private static final int NEW_MOVIE_ID = -1;

	private int id;
	private String name;

	public Movie(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Movie(String name) {
		this.id = NEW_MOVIE_ID;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
