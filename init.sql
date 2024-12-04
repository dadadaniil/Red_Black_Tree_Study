-- Create the database
CREATE DATABASE tree_management;

-- Switch to the database
-- \c tree_management;

-- Create the Files table
CREATE TABLE Files (
                       file_id SERIAL PRIMARY KEY,          -- Unique identifier for each file
                       file_name VARCHAR(255) NOT NULL,     -- Name of the file
                       file_path VARCHAR(255) NOT NULL,     -- Path to the file in the file system
                       upload_date TIMESTAMP DEFAULT NOW(), -- Timestamp when the file was uploaded
                       file_metadata JSONB                  -- Additional metadata about the file
);

-- Create the Trees table
CREATE TABLE Trees (
                       tree_id SERIAL PRIMARY KEY,          -- Unique identifier for each tree
                       file_id INTEGER NOT NULL,            -- Foreign key referencing Files
                       tree_type VARCHAR(50) NOT NULL,      -- Type of tree (e.g., Red-Black Tree)
                       creation_date TIMESTAMP DEFAULT NOW(), -- Timestamp when the tree was created
                       node_count INTEGER,                  -- Total number of nodes in the tree
                       tree_structure JSONB,                -- Serialized tree structure
                       CONSTRAINT fk_file FOREIGN KEY (file_id) REFERENCES Files (file_id) ON DELETE CASCADE
);

-- Create the PerformanceLogs table
CREATE TABLE PerformanceLogs (
                                 log_id SERIAL PRIMARY KEY,           -- Unique identifier for each log entry
                                 tree_id INTEGER NOT NULL,            -- Foreign key referencing Trees
                                 operation_type VARCHAR(50) NOT NULL, -- Type of operation (e.g., INSERT, DELETE, SEARCH)
                                 operation_duration FLOAT NOT NULL,   -- Duration of the operation in milliseconds
                                 operation_timestamp TIMESTAMP DEFAULT NOW(), -- When the operation was performed
                                 CONSTRAINT fk_tree FOREIGN KEY (tree_id) REFERENCES Trees (tree_id) ON DELETE CASCADE
);

-- Create indexes for efficient querying
CREATE INDEX idx_file_name ON Files (file_name);
CREATE INDEX idx_tree_type ON Trees (tree_type);
CREATE INDEX idx_operation_type ON PerformanceLogs (operation_type);
