USE movierating;

CREATE TABLE account(
	id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role enum('USER', 'ADMIN')
);

CREATE TABLE movie(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE rating(
	account_id INT,
	movie_id INT,
    rating INT,
    FOREIGN KEY(movie_id) REFERENCES movie(id) ON DELETE CASCADE,
    FOREIGN KEY(account_id) REFERENCES account(id) ON DELETE CASCADE
);

CREATE TABLE account_movie(
	account_id INT,
    movie_id INT,
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE,
    UNIQUE(account_id, movie_id)
);

# Guest account
INSERT INTO account VALUES(-1, NULL, NULL, 'USER');
# Admin account
INSERT INTO account(email, password, role) VALUES(0, "admin@test.com", "admin", 'ADMIN');