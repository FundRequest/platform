ALTER TABLE referral
  ADD COLUMN creation_date TIMESTAMP;
ALTER TABLE referral
  ADD COLUMN last_modified_date TIMESTAMP;
ALTER TABLE referral
  ADD COLUMN created_by VARCHAR(100);
ALTER TABLE referral
  ADD COLUMN last_modified_by VARCHAR(100);