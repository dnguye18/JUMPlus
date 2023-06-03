package com.movieratings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.movieratings.enums.AccountRole;
import com.movieratings.model.Account;
import com.movieratings.utility.ConnManager;

public class AccountDAOClass implements AccountDAO {

	private final static int ACCOUNT_INSERTED = 1;

	private Connection conn = ConnManager.getConnection();

	@Override
	public Account getAccountByEmailAndPassword(String email, String password) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE email = ? AND password = ?");
			stmt.setString(1, email);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int id = rs.getInt(1);
				AccountRole role = AccountRole.valueOf(rs.getString(4));
				return new Account(id, email, password, role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Account addAccount(Account account) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO account(email, password, role) VALUES(?, ?, ?)");
			stmt.setString(1, account.getEmail());
			stmt.setString(2, account.getPassword());
			stmt.setString(3, account.getRole().toString());

			int result = stmt.executeUpdate();

			if (result == ACCOUNT_INSERTED) {
				stmt = conn.prepareStatement("SELECT * FROM account WHERE email = ? AND password = ?");
				stmt.setString(1, account.getEmail());
				stmt.setString(2, account.getPassword());

				ResultSet rs = stmt.executeQuery();
				rs.next();
				account.setId(rs.getInt(1));
				return account;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
