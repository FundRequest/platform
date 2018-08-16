package io.fundrequest.platform.twitter;

import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.social.twitter.api.Twitter;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TweetRequestFundedHandlerTest {

    private TweetRequestFundedHandler handler;

    private Twitter twitter;
    private ITemplateEngine githubTemplateEngine;
    private FundService fundService;
    private String platformBasePath = "https://afdsgd";

    @BeforeEach
    void setUp() {
        twitter = mock(Twitter.class, RETURNS_DEEP_STUBS);
        githubTemplateEngine = mock(ITemplateEngine.class);
        fundService = mock(FundService.class);
        handler = new TweetRequestFundedHandler(twitter, githubTemplateEngine, fundService, platformBasePath);
    }

    @Test
    public void handle() {
        long fundId = 243L;
        long requestId = 764L;
        final String message = "ewfegdbf";
        final RequestFundedNotificationDto notification = RequestFundedNotificationDto.builder().fundId(fundId).requestId(requestId).build();
        final TokenValueDto tokenValue = TokenValueDtoMother.FND().build();
        final FundDto fund = FundDto.builder().tokenValue(tokenValue).build();
        final Context context = new Context();
        context.setVariable("platformBasePath", platformBasePath);
        context.setVariable("fund", fund);

        when(fundService.findOne(fundId)).thenReturn(fund);

        when(githubTemplateEngine.process(eq("notification-templates/request-funded-tweet"), refEq(context, "locale"))).thenReturn(message);

        handler.handle(notification);

        verify(twitter.timelineOperations()).updateStatus(message);
    }
}
