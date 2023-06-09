package com.dollarsbank.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnManager {
	private static Connection connection = null;

    private static final String URL = "jdbc:mysql://localhost:3306/dollarsbank";

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static void makeConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println("Could not connect to db");
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            makeConnection();
        }
        return connection;
    }
}
