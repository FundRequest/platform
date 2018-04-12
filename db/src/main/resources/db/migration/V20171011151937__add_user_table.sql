CREATE TABLE user (
  id                 VARCHAR(100),
  creation_date      TIMESTAMP,
  last_modified_date TIMESTAMP,
  created_by         VARCHAR(1000),
  last_modified_by   VARCHAR(1000),
  email              VARCHAR(200),
  phone_number       VARCHAR(200),
  PRIMARY KEY (id)
);
