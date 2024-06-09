package com.oragan.posSystem.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Connect to the SQLite database file (replace "/path/to/your/database.db" with the actual path)
            connection = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw e; // rethrow the exception to indicate failure
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found");
            throw e; // rethrow the exception to indicate failure
        }
    }

    public static synchronized DBConnection getInstance() throws SQLException, ClassNotFoundException {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
