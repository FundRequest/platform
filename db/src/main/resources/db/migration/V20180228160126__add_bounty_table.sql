CREATE TABLE bounty (
  id      BIGINT NOT NULL AUTO_INCREMENT,
  user_id VARCHAR(100),
  type    VARCHAR(100),
  status  VARCHAR(100),
  PRIMARY KEY (id)
);