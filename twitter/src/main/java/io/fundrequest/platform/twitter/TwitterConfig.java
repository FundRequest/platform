package io.fundrequest.platform.twitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfig {

    @Bean
    public Twitter tweetOnFundTwitterTemplate(@Value("${io.fundrequest.notifications.twitter.tweet-on-fund.consumerKey}") final String consumerKey,
                                              @Value("${io.fundrequest.notifications.twitter.tweet-on-fund.consumerSecret}") final String consumerSecret,
                                              @Value("${io.fundrequest.notifications.twitter.tweet-on-fund.accessToken}") final String accessToken,
                                              @Value("${io.fundrequest.notifications.twitter.tweet-on-fund.accessTokenSecret}") final String accessTokenSecret) {

        return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }
}
