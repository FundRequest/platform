package io.fundrequest.core.request.handler;

import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MakeUserAddressVerifiedOnFundedHandler {

    private final KeycloakRepository keycloakRepository;

    public MakeUserAddressVerifiedOnFundedHandler(final KeycloakRepository keycloakRepository) {
        this.keycloakRepository = keycloakRepository;
    }

    @EventListener
    public void handle(final RequestFundedEvent event) {
        final FundDto fund = event.getFundDto();
        final String funderUserId = fund.getFunderUserId();

        if (fund.getFunderAddress().equalsIgnoreCase(keycloakRepository.getEtherAddress(funderUserId))) {
            keycloakRepository.updateEtherAddressVerified(funderUserId, true);
        }
    }
}
