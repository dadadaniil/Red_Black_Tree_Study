package org.example.utils.database;

import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

@Log4j2
public class DatabaseHandler {

    protected String dbUrl;
    protected String user;
    protected String password;

    public DatabaseHandler() {
        loadDatabaseProperties();
    }

    public void logPerformance(String operationType, double durationMs) {
        String sql = "INSERT INTO PerformanceLogs ( operation_type, operation_duration) VALUES ( ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, operationType);
            stmt.setDouble(2, durationMs);

            stmt.executeUpdate();
            log.info("Performance logged: Operation = {}, Duration = {} ms", operationType, durationMs);

        } catch (SQLException e) {
            log.error("Failed to log performance: {}", e.getMessage());
        }
    }

    public int saveTree(int fileId, String treeType, int nodeCount, String treeStructureJson) {
        String sql = "INSERT INTO Trees (file_id, tree_type, node_count, tree_structure) VALUES (?, ?, ?, ?) RETURNING tree_id";

        try (Connection conn = getConnection();
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
        return -1;
    }

    public int saveFile(String fileName, String filePath, String metadataJson) {
        String sql = "INSERT INTO Files (file_name, file_path, file_metadata) VALUES (?, ?, ?) RETURNING file_id";

        try (Connection conn = getConnection();
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
        return -1;
    }

    protected void loadDatabaseProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
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

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }
}
