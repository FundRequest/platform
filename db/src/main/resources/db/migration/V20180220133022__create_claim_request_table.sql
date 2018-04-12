CREATE TABLE request_claim
(
  id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
  creation_date      TIMESTAMP,
  last_modified_date TIMESTAMP,
  created_by         VARCHAR(1000),
  last_modified_by   VARCHAR(1000),
  solver             VARCHAR(1000) NOT NULL,
  address            VARCHAR(50)   NOT NULL,
  status             VARCHAR(50)   NOT NULL,
  request_id         BIGINT        NOT NULL,
  flagged            NUMERIC       NOT NULL,
  CONSTRAINT request_claim_request_fk FOREIGN KEY (request_id) REFERENCES request (id)
);