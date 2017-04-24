CREATE TABLE request_technology (
  request_id BIGINT,
  technology VARCHAR(500),
  PRIMARY KEY (request_id, technology),
  CONSTRAINT fk_technology_request FOREIGN KEY (request_id) REFERENCES request (id)
);

CREATE INDEX idx_request_technology_01
  ON request_technology (request_id);