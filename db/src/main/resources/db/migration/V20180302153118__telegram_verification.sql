CREATE TABLE telegram_verifications (
  id            BIGINT(20) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  telegram_name VARCHAR(100)              NOT NULL,
  secret        VARCHAR(40)               NOT NULL,
  verified      BOOL     DEFAULT FALSE,
  last_action   DATETIME DEFAULT NULL
);

INSERT INTO telegram_verifications (
  telegram_name, secret
) VALUES ('QuintenDes', 'QDS_FTW');