package org.example.utils.database;

import lombok.extern.log4j.Log4j2;

import java.sql.*;

@Log4j2
public class DatabaseHandler {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    public void logPerformance(int treeId, String operationType, long durationNs) {
        String sql = "INSERT INTO PerformanceLogs (tree_id, operation_type, operation_duration) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, treeId);
            stmt.setString(2, operationType);
            stmt.setDouble(3, durationNs / 1_000_000.0); // Convert nanoseconds to milliseconds

            stmt.executeUpdate();
            log.info("Performance logged: TreeID = {}, Operation = {}, Duration = {} ms", treeId, operationType, durationNs / 1_000_000.0);

        } catch (SQLException e) {
            log.error("Failed to log performance: {}", e.getMessage());
        }
    }


    public int saveTree(int fileId, String treeType, int nodeCount, String treeStructureJson) {
        String sql = "INSERT INTO Trees (file_id, tree_type, node_count, tree_structure) VALUES (?, ?, ?, ?::jsonb) RETURNING tree_id";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fileId);
            stmt.setString(2, treeType);
            stmt.setInt(3, nodeCount);
            stmt.setString(4, treeStructureJson);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int treeId = rs.getInt(1);
                log.info("Tree saved successfully with ID: {}", treeId);
                return treeId;
            }
        } catch (SQLException e) {
            log.error("Failed to save tree: {}", e.getMessage());
        }
        return -1; // Error
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
                int fileId = rs.getInt(1);
                log.info("File saved successfully with ID: {}", fileId);
                return fileId;
            }
        } catch (SQLException e) {
            log.error("Failed to save file: {}", e.getMessage());
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
