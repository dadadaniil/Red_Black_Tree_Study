package org.example.utils.database;

import lombok.extern.log4j.Log4j2;

import java.sql.*;

@Log4j2
public class DatabaseHandler {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    public void saveTreeAction(String actionType, long durationMs, String filePath) {
        String sql = "INSERT INTO performancelogs (action_type, duration_ms, file_path, timestamp) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, actionType); // Action type (e.g., INSERT, DELETE)
            stmt.setLong(2, durationMs);   // Duration in milliseconds
            stmt.setString(3, filePath);  // File path from which the tree was created

            stmt.executeUpdate();
            log.info("Data saved successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int saveFile(String fileName, String filePath, String metadataJson) {
        String sql = "INSERT INTO Files (file_name, file_path, file_metadata) VALUES (?, ?, ?::jsonb) RETURNING file_id";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            stmt.setString(2, filePath);
            stmt.setString(3, metadataJson);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated file_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error
    }

    public void logPerformance(int treeId, String operationType, double durationMs) {
        String sql = "INSERT INTO PerformanceLogs (tree_id, operation_type, operation_duration) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, treeId);
            stmt.setString(2, operationType);
            stmt.setDouble(3, durationMs);

            stmt.executeUpdate();
            System.out.println("Operation logged successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
