CREATE TABLE twitter_posts (
  id                BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  content           TEXT       NOT NULL,
  verification_text TEXT       NOT NULL
);