package io.fundreqest.platform.tweb.fund;

import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;
import static java.math.BigDecimal.ZERO;

@Component
public class RefundValidator {

    private final RefundService refundService;
    private final FundService fundService;

    public RefundValidator(final RefundService refundService, final FundService fundService) {
        this.refundService = refundService;
        this.fundService = fundService;
    }

    public boolean isRefundable(final UserProfile userProfile,
                                final UserFundsDto fund,
                                final long requestId,
                                final String requestStatus) {
        return userProfile != null
               && userProfile.isEtherAddressVerified()
               && fund.getFunderAddress().equalsIgnoreCase(userProfile.getEtherAddress())
               && "FUNDED".equalsIgnoreCase(requestStatus)
               && !refundRequestAlreadyExists(requestId, userProfile.getEtherAddress())
               && hasPositiveBalance(requestId, fund);
    }

    private boolean refundRequestAlreadyExists(final long requestId, final String userAddress) {
        return refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)
                            .stream()
                            .map(RefundRequestDto::getFunderAddress)
                            .anyMatch(userAddress::equalsIgnoreCase);
    }

    private boolean hasPositiveBalance(long requestId, final UserFundsDto fund) {
        return hasPositiveBalance(requestId, fund, fund.getFndFunds()) || hasPositiveBalance(requestId, fund, fund.getOtherFunds());
    }

    private boolean hasPositiveBalance(final long requestId, final UserFundsDto fund, final TokenValueDto tokenValue) {
        if (tokenValue == null) {
            return false;
        }
        final Optional<TokenValueDto> fundsInContract = fundService.getFundsFor(requestId, fund.getFunderAddress(), tokenValue.getTokenAddress());
        return fundsInContract.map(tokenValue1 -> tokenValue1.getTotalAmount().compareTo(ZERO) > 0)
                              .orElse(false);
    }
}
