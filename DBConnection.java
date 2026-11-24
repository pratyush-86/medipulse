package com.medipulse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central JDBC connection factory.
 * Set the environment variable MEDIPULSE_DB_PASS, or edit the default below.
 */
public class DBConnection {
    private static final String URL =
        "jdbc:mysql://localhost:3306/medipulse_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = System.getenv().getOrDefault("MEDIPULSE_DB_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("MEDIPULSE_DB_PASS", "your_password");

    private DBConnection() { }

    public static Connection get() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
