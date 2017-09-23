ALTER TABLE fund
  ADD creation_date TIMESTAMP;
ALTER TABLE fund
  ADD last_modified_date TIMESTAMP;
ALTER TABLE fund
  ADD created_by VARCHAR(1000);
ALTER TABLE fund
  ADD last_modified_by VARCHAR(1000);