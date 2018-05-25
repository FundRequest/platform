DELETE FROM processed_blockchain_event;
ALTER TABLE processed_blockchain_event RENAME TO blockchain_event;
ALTER TABLE blockchain_event DROP PRIMARY KEY;
ALTER TABLE blockchain_event ADD COLUMN id BIGINT(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE blockchain_event ADD PRIMARY KEY (id);
ALTER TABLE blockchain_event ADD COLUMN log_index VARCHAR(2);
ALTER TABLE blockchain_event ADD CONSTRAINT UNIQUE (transaction_hash, log_index);

DELETE FROM fund;
ALTER TABLE fund ADD COLUMN blockchain_event_id VARCHAR(42) REFERENCES blockchain_event(id);

DELETE FROM claim;
ALTER TABLE claim ADD COLUMN blockchain_event_id VARCHAR(42) REFERENCES blockchain_event(id);
