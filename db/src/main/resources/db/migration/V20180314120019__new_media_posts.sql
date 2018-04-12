truncate table linkedin_post;

ALTER TABLE linkedin_post
  DROP COLUMN description;

ALTER TABLE linkedin_post
  ADD COLUMN description TEXT DEFAULT NULL;


INSERT INTO linkedin_post (comment, title, description, submitted_url, submitted_image_url)
VALUES ('Check out fundrequest.io!', 'fundrequest.io',
        'Want to start working on blockchain projects and be rewarded? Sign up on  @fundrequest!
#opensource #ethereum #fundrequest  #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=1',
        'https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=1',
        'https://cdn-images-1.medium.com/fit/c/120/120/1*PnVXfL_wAN1xSqJOOqzF4A.png');

INSERT INTO linkedin_post (comment, title, description, submitted_url, submitted_image_url)
VALUES ('Check out fundrequest.io!', 'fundrequest.io',
        'Just registered on  @fundrequest and it looks promising! I’m looking forward to the mainnet launch! #opensource #ethereum #fundrequest  #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=2',
        'https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=2',
        'https://cdn-images-1.medium.com/fit/c/120/120/1*PnVXfL_wAN1xSqJOOqzF4A.png');

INSERT INTO linkedin_post (comment, title, description, submitted_url, submitted_image_url)
VALUES ('Check out fundrequest.io!', 'fundrequest.io',
        'Pre-registered for the  @fundrequest platform. Development is cruising along nicely!  #fundrequest  #ethereum #opensource #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=3',
        'https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=3',
        'https://cdn-images-1.medium.com/fit/c/120/120/1*PnVXfL_wAN1xSqJOOqzF4A.png');

INSERT INTO linkedin_post (comment, title, description, submitted_url, submitted_image_url)
VALUES ('Check out fundrequest.io!', 'fundrequest.io',
        'Did you know @fundrequest is going live soon? I’ve already signed up for the platform and it already looks great!  #callingalldevelopers #fundrequest  #ethereum #opensource #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=4',
        'https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=4',
        'https://cdn-images-1.medium.com/fit/c/120/120/1*PnVXfL_wAN1xSqJOOqzF4A.png');

INSERT INTO linkedin_post (comment, title, description, submitted_url, submitted_image_url)
VALUES ('Check out fundrequest.io!', 'fundrequest.io',
        'Calling all developers, I just signed up on  @fundrequest , a platform that rewards code contributions. #fundrequest  #ethereum #opensource #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=5',
        'https://fundrequest.io?utm_source=besocial&utm_medium=linkedin&utm_campaign=early_signup&utm_content=5',
        'https://cdn-images-1.medium.com/fit/c/120/120/1*PnVXfL_wAN1xSqJOOqzF4A.png');

truncate table twitter_posts;

INSERT INTO twitter_posts (content, verification_text)
VALUES (
  'Calling all developers, I just signed up on @fundrequest_io , a platform that rewards code contributions. #fundrequest  #ethereum #opensource #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=twitter&utm_campaign=early_signup&utm_content=5',
  'I just signed up on');

INSERT INTO twitter_posts (content, verification_text)
VALUES (
  'Did you know @fundrequest_io is going live soon? I’ve already signed up for the platform and it already looks great!  #callingalldevelopers #fundrequest  #ethereum #opensource #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=twitter&utm_campaign=early_signup&utm_content=4',
  'signed up for the platform');

INSERT INTO twitter_posts (content, verification_text)
VALUES (
  'Pre-registered for the @fundrequest_io platform. Development is cruising along nicely!  #fundrequest  #ethereum #opensource #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=twitter&utm_campaign=early_signup&utm_content=3',
  'Pre-registered');

INSERT INTO twitter_posts (content, verification_text)
VALUES (
  'Just registered on @fundrequest_io and it looks promising! I’m looking forward to the mainnet launch! #opensource #ethereum #fundrequest  #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=twitter&utm_campaign=early_signup&utm_content=2',
  'Just registered on');

INSERT INTO twitter_posts (content, verification_text)
VALUES ('Want to start working on blockchain projects and be rewarded? Sign up on @fundrequest_io!
#opensource #ethereum #fundrequest  #callingalldevelopers https://fundrequest.io?utm_source=besocial&utm_medium=twitter&utm_campaign=early_signup&utm_content=1',
        'blockchain projects');
