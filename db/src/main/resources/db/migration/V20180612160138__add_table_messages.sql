CREATE TABLE messages (
  id                 BIGINT       NOT NULL AUTO_INCREMENT,
  creation_date      TIMESTAMP,
  last_modified_date TIMESTAMP,
  created_by         VARCHAR(1000),
  last_modified_by   VARCHAR(1000),
  name               VARCHAR(100) NOT NULL,
  type               VARCHAR(50)  NOT NULL,
  title              VARCHAR(500),
  description        VARCHAR(2000),
  link               VARCHAR(3000),
  PRIMARY KEY (id),

  CONSTRAINT name_type_unique UNIQUE (name, type)
);


INSERT INTO messages (name, type, title, description, link)
VALUES ('linkedin', 'REFERRAL_SHARE',
        'FundRequest Early-Signup',
        'To all the developers in my network, I’ve just registered on @fundrequest, great way for developers to get into blockchain. Early sign up is now open! ###REFLINK### #opensource #ethereum #fundrequest #callingalldevelopers https://fundrequest.io',
        'https://www.linkedin.com/shareArticle?mini=true&amp;url=###REFLINK###&amp;title=###TITLE###&amp;summary=###DESCRIPTION###&amp;source='
        );


INSERT INTO messages (name, type, title, description, link)
VALUES ('facebook', 'REFERRAL_SHARE',
        'FundRequest Early-Signup',
        'Just registered on @fundrequest_io, great way for #developers to get into #blockchain. Early sign up is now open! ###REFLINK### #opensource #ethereum #fundrequest #callingalldevelopers https://fundrequest.io',
        'https://www.facebook.com/sharer/sharer.php?u=###REFLINK###'
        );


INSERT INTO messages (name, type, title, description, link)
VALUES ('twitter', 'REFERRAL_SHARE',
        'FundRequest Early-Signup',
        'Just registered on @fundrequest_io, great way for #developers to get into #blockchain. Early sign up is now open! ###REFLINK### #opensource #ethereum #fundrequest #callingalldevelopers https://fundrequest.io',
        'https://twitter.com/home?status=###DESCRIPTION###'
        );