package io.fundrequest.platform.profile.config;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfig {

    @Bean
    public Twitter createTwitterInstance(@Value("${io.fundrequest.twitter.consumer-key}") final String consumerKey,
                                         @Value("${io.fundrequest.twitter.consumer-secret}") final String consumerSecret,
                                         @Value("${io.fundrequest.twitter.access-token}") final String accessToken,
                                         @Value("${io.fundrequest.twitter.access-token-secret}") final String accessTokenSecret) {
        val cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey(consumerKey)
          .setOAuthConsumerSecret(consumerSecret)
          .setOAuthAccessToken(accessToken)
          .setOAuthAccessTokenSecret(accessTokenSecret);
        val tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}
