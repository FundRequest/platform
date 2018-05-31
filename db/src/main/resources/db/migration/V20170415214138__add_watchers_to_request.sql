CREATE TABLE request_watcher (
  request_id BIGINT,
  email      VARCHAR(150),
  PRIMARY KEY (request_id, email),
  CONSTRAINT fk_watcher_request FOREIGN KEY (request_id) REFERENCES request (id)
);

CREATE INDEX idx_watcher_01
  ON request_watcher (request_id);