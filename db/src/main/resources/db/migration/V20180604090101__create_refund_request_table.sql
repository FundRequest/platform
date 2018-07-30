CREATE TABLE refund_request
(
  id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
  request_id              BIGINT        NOT NULL,
  funder_address          VARCHAR(50)   NOT NULL,
  requested_by            VARCHAR(1000),
  status                  VARCHAR(50)   NOT NULL,
  transaction_hash        VARCHAR(66),
  transaction_submit_time DATETIME,
  creation_date           TIMESTAMP,
  last_modified_date      TIMESTAMP,
  created_by              VARCHAR(1000),
  last_modified_by        VARCHAR(1000),
  CONSTRAINT request_refund_request_fk FOREIGN KEY (request_id) REFERENCES request (id)
);

CREATE INDEX idx_refund_request_01 ON refund_request (request_id);
CREATE INDEX idx_refund_request_02 ON refund_request (status);
CREATE INDEX idx_refund_request_03 ON refund_request (funder_address);
