package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.view.ClaimDtoMother;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PublishRequestClaimedNotificationHandlerTest {

    private PublishRequestClaimedNotificationHandler eventHandler;

    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        eventPublisher = mock(ApplicationEventPublisher.class);

        eventHandler = new PublishRequestClaimedNotificationHandler(eventPublisher);
    }

    @Test
    void onClaimed() {
        final RequestClaimedEvent claimedEvent = RequestClaimedEvent.builder()
                                                                    .claimDto(ClaimDtoMother.aClaimDto().build())
                                                                    .timestamp(LocalDateTime.now())
                                                                    .blockchainEventId(24L)
                                                                    .requestDto(RequestDtoMother.fundRequestArea51())
                                                                    .solver("sbsgdb")
                                                                    .build();
        final RequestClaimedNotificationDto expected = RequestClaimedNotificationDto.builder()
                                                                                    .blockchainEventId(claimedEvent.getBlockchainEventId())
                                                                                    .date(claimedEvent.getTimestamp())
                                                                                    .requestId(claimedEvent.getRequestDto().getId())
                                                                                    .claimId(claimedEvent.getClaimDto().getId())
                                                                                    .build();

        eventHandler.onClaimed(claimedEvent);

        verify(eventPublisher).publishEvent(refEq(expected, "uuid"));
    }
}