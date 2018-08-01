package io.fundrequest.platform.twitter;

import io.fundrequest.core.request.fund.FundService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.thymeleaf.ITemplateEngine;

@Configuration
@ConditionalOnProperty(name = "io.fundrequest.notifications.twitter.enabled", havingValue = "true")
public class TwitterConfig {

    @Bean
    public Twitter tweetOnFundTwitterTemplate(@Value("${io.fundrequest.notifications.twitter.tweet-on-fund.consumerKey}") final String consumerKey,
                                              @Value("${io.fundrequest.notifications.twitter.tweet-on-fund.consumerSecret}") final String consumerSecret,
                                              @Value("${io.fundrequest.notifications.twitter.tweet-on-fund.accessToken}") final String accessToken,
                                              @Value("${io.fundrequest.notifications.twitter.tweet-on-fund.accessTokenSecret}") final String accessTokenSecret) {

        return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    @Bean
    public TweetRequestFundedHandler tweetRequestFundedHandler(final Twitter tweetOnFundTwitterTemplate,
                                                               final ITemplateEngine githubTemplateEngine,
                                                               final FundService fundService,
                                                               @Value("${io.fundrequest.platform.base-path}") final String platformBasePath) {
        return new TweetRequestFundedHandler(tweetOnFundTwitterTemplate, githubTemplateEngine, fundService, platformBasePath);
    }
}
