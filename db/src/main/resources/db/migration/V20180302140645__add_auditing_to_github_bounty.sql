ALTER TABLE github_bounty
  ADD creation_date TIMESTAMP;
ALTER TABLE github_bounty
  ADD last_modified_date TIMESTAMP;
ALTER TABLE github_bounty
  ADD created_by VARCHAR(100);
ALTER TABLE github_bounty
  ADD last_modified_by VARCHAR(100);