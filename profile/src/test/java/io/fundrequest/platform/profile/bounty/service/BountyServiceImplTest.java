package io.fundrequest.platform.profile.bounty.service;

import io.fundrequest.platform.profile.bounty.domain.Bounty;
import io.fundrequest.platform.profile.bounty.domain.BountyType;
import io.fundrequest.platform.profile.bounty.dto.PaidBountyDto;
import io.fundrequest.platform.profile.bounty.infrastructure.BountyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BountyServiceImplTest {

    private final BountyServiceImpl bountyService;
    private final BountyRepository bountyRepository;

    public BountyServiceImplTest() {
        bountyRepository = mock(BountyRepository.class);
        bountyService = new BountyServiceImpl(bountyRepository);
    }

    @Test
    void getPaidBounties() {
        Principal principal = () -> "davyvanroy";
        Bounty bounty = Bounty.builder().type(BountyType.LINK_GITHUB).build();
        ReflectionTestUtils.setField(bounty, "transactionHash", "0x0");
        when(bountyRepository.findByUserId(principal.getName())).thenReturn(Collections.singletonList(bounty));

        List<PaidBountyDto> result = bountyService.getPaidBounties(principal);

        assertThat(result).hasSize(1);
    }
}