ALTER TABLE pending_funds
  ADD COLUMN added_date DATETIME DEFAULT now();
