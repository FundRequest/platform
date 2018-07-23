package io.fundrequest.platform.intercom.claim;

import io.fundrequest.core.request.claim.ClaimService;
import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import org.springframework.stereotype.Component;

@Component
public class RequestClaimedUserNotificationHandler {

    private final ClaimService claimService;

    public RequestClaimedUserNotificationHandler(final ClaimService claimService, KeycloakRepository keycloakRepository) {
        this.claimService = claimService;
    }

    public void handle(final RequestClaimedNotificationDto notification) {

    }
}
