package org.example.utils.database;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseHandlerFactory {

    public static AbstractDatabaseHandler getDatabaseHandler() {
        Properties props = new Properties();
        try (InputStream input = DatabaseHandlerFactory.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            props.load(input);
            String dbType = props.getProperty("db.type");
            if (dbType == null) {
                throw new RuntimeException("Database type not specified in application.properties");
            }
            return switch (dbType.toLowerCase()) {
                case "postgresql" -> new PostgreSQLDatabaseHandler();
                case "h2" -> new H2DatabaseHandler();
                case "mysql" -> new MySQLDatabaseHandler();
                default -> throw new RuntimeException("Unsupported database type: " + dbType);
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database handler: " + e.getMessage(), e);
        }
    }
}
