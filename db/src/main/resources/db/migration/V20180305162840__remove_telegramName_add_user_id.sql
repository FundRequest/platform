DELETE FROM telegram_verifications
WHERE telegram_name LIKE 'QuintenDes';

ALTER TABLE telegram_verifications
  ADD COLUMN user_id VARCHAR(100);