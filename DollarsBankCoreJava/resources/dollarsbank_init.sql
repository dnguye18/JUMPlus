USE dollarsbank;

CREATE TABLE account(
	id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(20) NOT NULL,
    pin VARCHAR(4) NOT NULL,
    balance DECIMAL(12,2)
);

CREATE TABLE customer(
	id INT,
    name VARCHAR(40) NOT NULL,
    address VARCHAR(255) NOT NULL,
    telephone_number CHAR(10) NOT NULL,
    FOREIGN KEY(id) REFERENCES account(id)
    ON DELETE CASCADE
);

CREATE TABLE transaction(
	id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    target_account_id INT DEFAULT -1,
    action ENUM('DEPOSIT', 'WITHDRAW', 'TRANSFER_IN', 'TRANSFER_OUT'),
    amount DECIMAL(12, 2) NOT NULL,
    new_balance DECIMAL(12, 2) NOT NULL,
    timestamp DATETIME NOT NULL
);