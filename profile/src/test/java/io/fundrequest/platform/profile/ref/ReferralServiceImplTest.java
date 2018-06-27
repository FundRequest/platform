package io.fundrequest.platform.profile.ref;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.profile.bounty.domain.BountyType;
import io.fundrequest.platform.profile.bounty.event.CreateBountyCommand;
import io.fundrequest.platform.profile.bounty.service.BountyService;
import io.fundrequest.platform.profile.ref.domain.Referral;
import io.fundrequest.platform.profile.ref.domain.ReferralStatus;
import io.fundrequest.platform.profile.ref.infrastructure.ReferralRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReferralServiceImplTest {

    private ReferralService referralService;
    private ReferralRepository referralRepository;
    private KeycloakRepository keycloakRepository;
    private BountyService bountyService;

    @BeforeEach
    void setUp() {
        referralRepository = mock(ReferralRepository.class);
        keycloakRepository = mock(KeycloakRepository.class);
        bountyService = mock(BountyService.class);
        referralService = new ReferralServiceImpl(
                referralRepository,
                keycloakRepository,
                bountyService,
                "googlekey"
        );
    }

    @Test
    void savesReferralAndCreatesBounty() {
        Principal referee = () -> "davyvanroy";
        CreateRefCommand command = CreateRefCommand.builder().principal(referee).ref("referrer").build();
        when(keycloakRepository.userExists(referee.getName())).thenReturn(true);
        when(keycloakRepository.userExists(command.getRef())).thenReturn(true);
        when(referralRepository.existsByReferee(referee.getName())).thenReturn(false);
        when(keycloakRepository.isVerifiedDeveloper(referee.getName())).thenReturn(true);


        referralService.createNewRef(command);


        verify(referralRepository, times(2)).save(Referral.builder()
                                                          .referrer(command.getRef())
                                                          .referee(referee.getName())
                                                          .status(ReferralStatus.PENDING)
                                                          .build());
        verify(bountyService).createBounty(CreateBountyCommand.builder()
                                                              .type(BountyType.REFERRAL)
                                                              .userId(command.getRef())
                                                              .build());


    }
}