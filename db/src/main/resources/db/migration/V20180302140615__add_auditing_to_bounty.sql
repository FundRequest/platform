ALTER TABLE bounty
  ADD creation_date TIMESTAMP;
ALTER TABLE bounty
  ADD last_modified_date TIMESTAMP;
ALTER TABLE bounty
  ADD created_by VARCHAR(100);
ALTER TABLE bounty
  ADD last_modified_by VARCHAR(100);