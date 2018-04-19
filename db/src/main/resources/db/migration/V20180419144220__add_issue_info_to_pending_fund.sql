ALTER TABLE pending_funds
  ADD owner VARCHAR(250);

ALTER TABLE pending_funds
  ADD repo VARCHAR(250);

ALTER TABLE pending_funds
  ADD issue_number VARCHAR(100);

ALTER TABLE pending_funds
  ADD title VARCHAR(2000);