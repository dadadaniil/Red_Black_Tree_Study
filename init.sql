CREATE
DATABASE tree_management;

CREATE TABLE Files
(
    file_id       SERIAL PRIMARY KEY,
    file_name     VARCHAR(255) NOT NULL,
    file_path     VARCHAR(255) NOT NULL,
    upload_date   TIMESTAMP DEFAULT NOW(),
    file_metadata JSONB
);

CREATE TABLE Trees
(
    tree_id        SERIAL PRIMARY KEY,
    file_id        INTEGER     NOT NULL,
    tree_type      VARCHAR(50) NOT NULL,
    creation_date  TIMESTAMP DEFAULT NOW() node_count INTEGER,
    tree_structure JSONB,
    CONSTRAINT fk_file FOREIGN KEY (file_id) REFERENCES Files (file_id) ON DELETE CASCADE
);

CREATE TABLE PerformanceLogs
(
    log_id              SERIAL PRIMARY KEY,
    operation_type      VARCHAR(50) NOT NULL,
    operation_duration  FLOAT       NOT NULL,
    operation_timestamp TIMESTAMP DEFAULT
);

CREATE INDEX idx_file_name ON Files (file_name);
CREATE INDEX idx_tree_type ON Trees (tree_type);
CREATE INDEX idx_operation_type ON PerformanceLogs (operation_type);
