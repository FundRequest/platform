package io.fundrequest.platform.intercom.claim;

import io.fundrequest.core.request.claim.ClaimService;
import io.fundrequest.core.request.view.ClaimDtoMother;
import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestClaimedUserNotificationHandlerTest {

    private RequestClaimedUserNotificationHandler handler;
    private ClaimService claimService;
    private KeycloakRepository keycloakRepository;

    @BeforeEach
    void setUp() {
        claimService = mock(ClaimService.class);
        keycloakRepository = mock(KeycloakRepository.class);
        handler = new RequestClaimedUserNotificationHandler(claimService, keycloakRepository);
    }

    @Test
    void handle() {
        final long blockchainEventId = 2435L;
        final long requestId = 54657L;
        final long claimId = 7657L;
        final RequestClaimedNotificationDto notification = RequestClaimedNotificationDto.builder()
                                                                                        .blockchainEventId(blockchainEventId)
                                                                                        .requestId(requestId)
                                                                                        .claimId(claimId)
                                                                                        .date(LocalDateTime.now())
                                                                                        .build();

        when(claimService.findOne(claimId)).thenReturn(Optional.of(ClaimDtoMother.aClaimDto().build()));
        when(keycloakRepository.getUser(""));


        handler.handle(notification);

    }
}