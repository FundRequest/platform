CREATE TABLE twitter_bounties (
  id             BIGINT(20)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
  start_date     DATETIME    NOT NULL DEFAULT now(),
  end_date       DATETIME             DEFAULT NULL,
  type           VARCHAR(20) NOT NULL,
  follow_account VARCHAR(100)         DEFAULT NULL,
  required_posts INT                  DEFAULT 1
);