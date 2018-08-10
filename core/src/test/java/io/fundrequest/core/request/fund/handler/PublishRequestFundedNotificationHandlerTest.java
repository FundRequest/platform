package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PublishRequestFundedNotificationHandlerTest {

    private PublishRequestFundedNotificationHandler eventHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    void setUp() {
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        eventHandler = new PublishRequestFundedNotificationHandler(applicationEventPublisher);
    }

    @Test
    public void onFunded() {
        final RequestFundedEvent fundedEvent = RequestFundedEvent.builder()
                                                                 .fundDto(FundDtoMother.aFundDto().build())
                                                                 .timestamp(LocalDateTime.now())
                                                                 .requestId(35L).build();
        final RequestFundedNotificationDto expected = RequestFundedNotificationDto.builder()
                                                                                  .blockchainEventId(fundedEvent.getFundDto().getBlockchainEventId())
                                                                                  .date(fundedEvent.getTimestamp())
                                                                                  .requestId(fundedEvent.getRequestId())
                                                                                  .fundId(fundedEvent.getFundDto().getId())
                                                                                  .build();

        eventHandler.onFunded(fundedEvent);

        verify(applicationEventPublisher).publishEvent(refEq(expected, "uuid"));
    }
}