CREATE TABLE request (
  id                 SERIAL PRIMARY KEY ,
  creation_date      TIMESTAMP,
  last_modified_date TIMESTAMP,
  created_by         INTEGER,
  last_modified_by   INTEGER,
  issue_link         VARCHAR(2000),
  label              VARCHAR(2000),
  status             VARCHAR(50),
  type               VARCHAR(50),
  source             VARCHAR(50)
);
