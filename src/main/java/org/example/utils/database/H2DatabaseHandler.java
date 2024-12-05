package org.example.utils.database;

import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Log4j2
public class H2DatabaseHandler extends AbstractDatabaseHandler {

    @Override
    protected void loadDatabaseProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("h2.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find h2.properties");
            }
            props.load(input);
            this.dbUrl = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
        } catch (Exception e) {
            log.error("Failed to load database properties: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }
}
