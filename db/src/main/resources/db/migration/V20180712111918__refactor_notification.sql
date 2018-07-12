#DDL
ALTER TABLE notification
  MODIFY notification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE notification
  ADD COLUMN claim_id bigint(20);

ALTER TABLE notification
  ADD COLUMN uuid VARCHAR(36);

ALTER TABLE notification
  DROP solver;

#DML
UPDATE notification n
SET claim_id = (SELECT c.id
                FROM claim c
                WHERE n.blockchain_event_id = c.blockchain_event_id);

UPDATE notification n
SET request_id = (SELECT f.request_id
                  FROM fund f
                  WHERE n.fund_id = f.id)
WHERE n.fund_id IS NOT NULL;

UPDATE notification n
SET uuid = UUID()
WHERE uuid IS NULL;
