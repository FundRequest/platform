ALTER TABLE request_claim
  ADD COLUMN transaction_hash VARCHAR(66) DEFAULT NULL;

ALTER TABLE request_claim
  ADD COLUMN transaction_submit_time DATETIME DEFAULT NULL;