ALTER TABLE claim
  ADD COLUMN token_address VARCHAR(42) DEFAULT '0x02f96ef85cad6639500ca1cc8356f0b5ca5bf1d2';

UPDATE claim as c
SET c.token_address = (SELECT f.token
                       FROM fund as f
                       WHERE f.request_id = c.request_id
                       GROUP BY f.token)
WHERE c.id IN (SELECT c1.id
               FROM claim as c1
                 JOIN fund as f1 ON f1.request_id = c1.request_id
               GROUP BY c1.id
               HAVING COUNT(DISTINCT f1.token) = 1);

ALTER TABLE claim
  ADD COLUMN transaction_hash VARCHAR(66) DEFAULT NULL;

UPDATE claim as c
SET c.transaction_hash = (SELECT rc.transaction_hash
                          FROM request_claim as rc
                          WHERE rc.request_id = c.request_id);