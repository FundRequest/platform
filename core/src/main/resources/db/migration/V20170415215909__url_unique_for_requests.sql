DELETE FROM request_watcher;
DELETE FROM request;

ALTER TABLE request MODIFY issue_link VARCHAR(255);

CREATE UNIQUE INDEX idx_request_01
  ON request (issue_link);