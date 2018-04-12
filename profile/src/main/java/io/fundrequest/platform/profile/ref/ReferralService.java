package io.fundrequest.platform.profile.ref;

import io.fundrequest.platform.profile.ref.dto.ReferralDto;
import io.fundrequest.platform.profile.ref.dto.ReferralOverviewDto;

import java.security.Principal;
import java.util.List;

public interface ReferralService {
    ReferralOverviewDto getOverview(Principal principal);

    String generateRefLink(String userId, String source);

    List<ReferralDto> getReferrals(Principal principal);

    void createNewRef(CreateRefCommand command);
}
