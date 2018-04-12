CREATE TABLE linkedin_verification (
  id       BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id  VARCHAR(100),
  post_url VARCHAR(500)
);