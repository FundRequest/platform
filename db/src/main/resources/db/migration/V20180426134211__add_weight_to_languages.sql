ALTER TABLE request_technology
  ADD weight DECIMAL(32);

UPDATE request_technology
SET weight = 1;