CREATE TABLE persistence_foundation_probe (
    probe_id VARCHAR(120) PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
