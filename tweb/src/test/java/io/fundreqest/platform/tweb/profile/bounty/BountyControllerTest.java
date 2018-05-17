package io.fundreqest.platform.tweb.profile.bounty;

import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.core.PrincipalMother;
import io.fundrequest.platform.profile.bounty.domain.BountyType;
import io.fundrequest.platform.profile.bounty.dto.PaidBountyDto;
import io.fundrequest.platform.profile.bounty.service.BountyService;
import io.fundrequest.platform.profile.github.GithubBountyService;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.stackoverflow.StackOverflowBountyService;
import io.fundrequest.platform.profile.survey.domain.SurveyService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class BountyControllerTest extends AbstractControllerTest<BountyController> {

    private BountyService bountyService;

    @Override
    protected BountyController setupController() {
        bountyService = mock(BountyService.class);
        return new BountyController(
                mock(SurveyService.class),
                mock(GithubBountyService.class),
                mock(StackOverflowBountyService.class),
                bountyService,
                mock(ProfileService.class)
        );
    }

    @Test
    void rewardsReturnsPaidBounties() throws Exception {
        Principal principal = PrincipalMother.davyvanroy();

        List<PaidBountyDto> expectedPaidBounties = Collections.singletonList(PaidBountyDto.builder().type(BountyType.LINK_GITHUB).build());
        when(bountyService.getPaidBounties(principal)).thenReturn(expectedPaidBounties);

        this.mockMvc.perform(get("/profile/rewards").principal(principal))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(model().attribute("paidBounties", expectedPaidBounties))
                    .andExpect(MockMvcResultMatchers.view().name("pages/profile/rewards"));
    }
}