package com.MapFlow.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * To use DatabaseConfig, you must add the appropriate settings to the app.properties file:
 * orm.datasource.url
 * orm.datasource.username
 * orm.datasource.password
 * orm.datasource.driver
 **/

public class ConfigurationManager {
    private final String url;
    private final String username;
    private final String password;
    private final String driver;

    public ConfigurationManager() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (input == null) {
                throw new RuntimeException("File 'app.properties' not found in resources!");
            }
            properties.load(input);

            this.url = properties.getProperty("mapflow.datasource.url");
            this.username = properties.getProperty("mapflow.datasource.username");
            this.password = properties.getProperty("mapflow.datasource.password");
            this.driver = properties.getProperty("mapflow.datasource.driver");
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
