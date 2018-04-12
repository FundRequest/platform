CREATE TABLE linkedin_post (
  id                  BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  comment             VARCHAR(700),
  title               VARCHAR(200),
  description         VARCHAR(256),
  submitted_url       VARCHAR(1000),
  submitted_image_url VARCHAR(1000)
);

INSERT INTO linkedin_post (comment, title, description, submitted_url, submitted_image_url)
VALUES ('Check out fundrequest.io!', 'fundrequest.io',
        'A Decentralized Marketplace for Open Source Collaboration.', 'https://blog.fundrequest.io',
        'https://cdn-images-1.medium.com/fit/c/120/120/1*PnVXfL_wAN1xSqJOOqzF4A.png');