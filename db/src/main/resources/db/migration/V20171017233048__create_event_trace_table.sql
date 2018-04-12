CREATE TABLE processed_blockchain_event (
  transaction_hash VARCHAR(200),
  process_date     TIMESTAMP,
  PRIMARY KEY (transaction_hash)
);
