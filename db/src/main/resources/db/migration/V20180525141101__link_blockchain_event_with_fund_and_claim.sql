DROP TABLE processed_blockchain_event;

CREATE TABLE blockchain_event (
  id               BIGINT(20) NOT NULL AUTO_INCREMENT,
  transaction_hash varchar(200),
  log_index        VARCHAR(4),
  process_date     TIMESTAMP,
  PRIMARY KEY (id)
);

DELETE FROM fund;
ALTER TABLE fund ADD COLUMN blockchain_event_id VARCHAR(42) REFERENCES blockchain_event(id);

DELETE FROM claim;
ALTER TABLE claim ADD COLUMN blockchain_event_id VARCHAR(42) REFERENCES blockchain_event(id);
ALTER TABLE claim ADD COLUMN token_hash VARCHAR(50);

DELETE FROM notification;
ALTER TABLE notification ADD COLUMN blockchain_event_id VARCHAR(42) REFERENCES blockchain_event(id);
ALTER TABLE notification DROP COLUMN transaction_id;
