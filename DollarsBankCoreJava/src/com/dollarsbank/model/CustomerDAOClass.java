package com.dollarsbank.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dollarsbank.utility.ConnManager;

public class CustomerDAOClass implements CustomerDAO {
	
	private final static int ROW_UPDATED = 1;
	
	Connection conn = ConnManager.getConnection();

	@Override
	public Customer getCustomerById(int id) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customer WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				String name = rs.getString(2);
				String address = rs.getString(3);
				String number = rs.getString(4);
				
				return new Customer(id, name, address, number);
			} else {
				return null;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Customer addCustomer(Customer customer) {
		try {
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO "
					+ "customer VALUES (?, ?, ?, ?)");
			stmt.setInt(1, customer.getId());
			stmt.setString(2, customer.getName());
			stmt.setString(3, customer.getAddress());
			stmt.setString(4, customer.getPhoneNumber());
			
			int result = stmt.executeUpdate();
			
			if (result == ROW_UPDATED) {
				return customer;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
