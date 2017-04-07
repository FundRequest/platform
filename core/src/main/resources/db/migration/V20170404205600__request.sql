CREATE TABLE request (
  id                 BIGINT NOT NULL AUTO_INCREMENT,
  creation_date      TIMESTAMP,
  last_modified_date TIMESTAMP,
  created_by         VARCHAR(1000),
  last_modified_by   VARCHAR(1000),
  issue_link         VARCHAR(2000),
  label              VARCHAR(2000),
  status             VARCHAR(50),
  type               VARCHAR(50),
  source             VARCHAR(50),
  PRIMARY KEY (id)
);
