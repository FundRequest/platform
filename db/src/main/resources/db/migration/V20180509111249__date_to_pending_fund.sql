ALTER TABLE pending_funds
  ADD creation_date TIMESTAMP;
ALTER TABLE pending_funds
  ADD last_modified_date TIMESTAMP;
ALTER TABLE pending_funds
  ADD created_by VARCHAR(100);
ALTER TABLE pending_funds
  ADD last_modified_by VARCHAR(100);