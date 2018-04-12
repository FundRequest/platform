CREATE TABLE notification (
  id                BIGINT PRIMARY KEY AUTO_INCREMENT,
  notification_date TIMESTAMP,
  type              VARCHAR(50),
  request_id        BIGINT,
  fund_id           BIGINT
);