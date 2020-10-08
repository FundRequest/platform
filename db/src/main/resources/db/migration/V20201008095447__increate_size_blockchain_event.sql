ALTER TABLE blockchain_event MODIFY COLUMN log_index VARCHAR(50);
ALTER TABLE blockchain_event MODIFY COLUMN transaction_hash VARCHAR(400);
