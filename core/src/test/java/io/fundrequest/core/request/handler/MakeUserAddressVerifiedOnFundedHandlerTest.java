package io.fundrequest.core.request.handler;

import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MakeUserAddressVerifiedOnFundedHandlerTest {

    private MakeUserAddressVerifiedOnFundedHandler handler;
    private KeycloakRepository keycloakRepository;

    @BeforeEach
    void setUp() {
        keycloakRepository = mock(KeycloakRepository.class);
        handler = new MakeUserAddressVerifiedOnFundedHandler(keycloakRepository);

    }

    @Test
    public void makeUserAddressVerified() {
        final String funderAddress = "0xa23be76";
        final String funderUserId = "aefgd13";
        final RequestFundedEvent event = RequestFundedEvent.builder()
                                                           .fundDto(FundDto.builder().funderUserId(funderUserId).funderAddress(funderAddress).build())
                                                           .requestId(23L)
                                                           .timestamp(LocalDateTime.now())
                                                           .build();

        when(keycloakRepository.getEtherAddress(funderUserId)).thenReturn(funderAddress);

        handler.handle(event);

        verify(keycloakRepository).updateEtherAddressVerified(funderUserId, true);
    }

    @Test
    public void handle_fundAddressNeUserAddress() {
        final String funderAddress = "0xa2346876";
        final String funderUserId = "aefs42";
        final RequestFundedEvent event = RequestFundedEvent.builder()
                                                           .fundDto(FundDto.builder().funderUserId(funderUserId).funderAddress(funderAddress).build())
                                                           .requestId(23L)
                                                           .timestamp(LocalDateTime.now())
                                                           .build();

        when(keycloakRepository.getEtherAddress(funderUserId)).thenReturn(funderAddress + "aaa");

        handler.handle(event);

        verify(keycloakRepository, never()).updateEtherAddressVerified(funderUserId, true);
    }
}
