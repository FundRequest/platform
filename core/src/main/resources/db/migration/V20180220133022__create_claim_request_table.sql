CREATE TABLE request_claim
(
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  solver     VARCHAR(1000) NOT NULL,
  address    VARCHAR(50
             )   NOT NULL,
  status     VARCHAR(50)   NOT NULL,
  request_id BIGINT        NOT NULL,
  flagged    NUMERIC       NOT NULL,
  CONSTRAINT claim_request_id_fk FOREIGN KEY (request_id) REFERENCES request (id)
);