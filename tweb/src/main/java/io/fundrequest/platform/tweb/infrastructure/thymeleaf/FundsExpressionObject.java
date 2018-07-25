package io.fundrequest.platform.tweb.infrastructure.thymeleaf;

import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import io.fundrequest.platform.tweb.fund.RefundValidator;
import org.springframework.stereotype.Component;

@Component
public class FundsExpressionObject {

    private final RefundValidator refundValidator;

    public FundsExpressionObject(final RefundValidator refundValidator) {
        this.refundValidator = refundValidator;
    }

    public boolean isRefundable(final UserProfile userProfile, final UserFundsDto fund, final long requestId, final String requestStatus) {
        return refundValidator.isRefundable(userProfile, fund, requestId, requestStatus);
    }
}
