CREATE TABLE twitter_bounty_fulfillments (
  id               BIGINT(20)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username         VARCHAR(100) NOT NULL,
  user_id          VARCHAR(100) NOT NULL,
  bounty_id        BIGINT(20)   NOT NULL,
  fulfillment_date DATETIME     NOT NULL,
  FOREIGN KEY (bounty_id) REFERENCES twitter_bounties (id)
);


