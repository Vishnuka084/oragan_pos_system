package com.oragan.posSystem.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw e;
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found");
            throw e;
        }
    }

    public static synchronized DBConnection getInstance() throws SQLException, ClassNotFoundException {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        } else if (dbConnection.getConnection().isClosed()) {
            dbConnection = new DBConnection();  // Reinitialize if the connection is closed
            System.out.println("Database connection reinitialized.");
        }
        return dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
