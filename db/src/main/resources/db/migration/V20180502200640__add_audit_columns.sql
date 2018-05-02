ALTER TABLE claim
  ADD creation_date TIMESTAMP;
ALTER TABLE claim
  ADD last_modified_date TIMESTAMP;
ALTER TABLE claim
  ADD created_by VARCHAR(100);
ALTER TABLE claim
  ADD last_modified_by VARCHAR(100);