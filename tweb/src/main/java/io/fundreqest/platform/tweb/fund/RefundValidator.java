package io.fundreqest.platform.tweb.fund;

import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.springframework.stereotype.Component;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;

@Component
public class RefundValidator {

    private final RefundService refundService;

    public RefundValidator(final RefundService refundService) {
        this.refundService = refundService;
    }

    public boolean isRefundable(final UserProfile userProfile, final UserFundsDto fund, final long requestId, final String requestStatus) {
        return userProfile != null
               && userProfile.isEtherAddressVerified()
               && fund.isRefundable()
               && "FUNDED".equalsIgnoreCase(requestStatus)
               && fund.getFunderAddress().equalsIgnoreCase(userProfile.getEtherAddress())
               && refundRequestAlreadyExists(requestId, userProfile.getEtherAddress());
    }

    private boolean refundRequestAlreadyExists(final long requestId, final String userAddress) {
        return refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)
                            .stream()
                            .map(RefundRequestDto::getFunderAddress)
                            .noneMatch(funderAddress -> funderAddress.equalsIgnoreCase(userAddress));
    }
}