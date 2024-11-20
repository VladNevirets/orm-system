package com.orm.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private String url;
    private String username;
    private String password;
    private String driver;

    public DatabaseConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (input == null) {
                throw new RuntimeException("File 'app.properties' not found in resources!");
            }
            properties.load(input);

            this.url = properties.getProperty("datasource.url");
            this.username = properties.getProperty("datasource.username");
            this.password = properties.getProperty("datasource.password");
            this.driver = properties.getProperty("datasource.driver");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        }
        return DriverManager.getConnection(url, username, password);
    }

}
