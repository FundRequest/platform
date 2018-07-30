CREATE TABLE refund
(
  id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
  funder_address      VARCHAR(50) NOT NULL,
  amount_in_wei       DECIMAL(50) NOT NULL,
  token_hash          VARCHAR(50) NOT NULL,
  request_id          BIGINT      NOT NULL,
  requested_by        VARCHAR(100),
  blockchain_event_id BIGINT(20)  NOT NULL,
  creation_date       TIMESTAMP   NOT NULL,
  last_modified_date  TIMESTAMP,
  created_by          VARCHAR(1000),
  last_modified_by    VARCHAR(1000),
  CONSTRAINT refund_request_fk FOREIGN KEY (request_id) REFERENCES request (id),
  CONSTRAINT refund_blockchain_event_fk FOREIGN KEY (blockchain_event_id) REFERENCES blockchain_event (id)
);

CREATE INDEX idx_refund_01 ON refund (request_id);
CREATE INDEX idx_refund_02 ON refund (blockchain_event_id);
