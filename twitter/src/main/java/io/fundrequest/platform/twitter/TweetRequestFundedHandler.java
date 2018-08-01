package io.fundrequest.platform.twitter;

import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;


@Component
public class TweetRequestFundedHandler {

    private final Twitter tweetOnFundTwitterTemplate;
    private final ITemplateEngine githubTemplateEngine;
    private final FundService fundService;
    private final String platformBasePath;

    public TweetRequestFundedHandler(final Twitter tweetOnFundTwitterTemplate,
                                     final ITemplateEngine githubTemplateEngine,
                                     final FundService fundService,
                                     @Value("${io.fundrequest.platform.base-path}") final String platformBasePath) {
        this.tweetOnFundTwitterTemplate = tweetOnFundTwitterTemplate;
        this.githubTemplateEngine = githubTemplateEngine;
        this.fundService = fundService;
        this.platformBasePath = platformBasePath;
    }

    @EventListener
    @Async("taskExecutor")
    @Transactional(readOnly = true, propagation = REQUIRES_NEW)
    public void handle(final RequestFundedNotificationDto notification) {
        final FundDto fund = fundService.findOne(notification.getFundId());
        Context context = new Context();
        context.setVariable("platformBasePath", platformBasePath);
        context.setVariable("fund", fund);

        final String message = githubTemplateEngine.process("notification-templates/request-funded-tweet", context);
        tweetOnFundTwitterTemplate.timelineOperations().updateStatus(message);
    }
}
