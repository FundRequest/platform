CREATE INDEX idx_request_claim_01
  ON request_claim (status);

CREATE INDEX idx_request_claim_02
  ON request_claim (creation_date ASC);

CREATE INDEX idx_request_claim_03
  ON request_claim (last_modified_date DESC);