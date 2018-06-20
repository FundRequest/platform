package io.fundrequest.core.request.fund;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserFundsDto {

    private String funderAddress;
    private String funderUserId;
    private TokenValueDto fndFunds;
    private TokenValueDto otherFunds;
    private TokenValueDto fndRefunds;
    private TokenValueDto otherRefunds;

    public boolean hasRefunds() {
        return fndRefunds != null || otherRefunds != null;
    }

    public boolean isRefundable() {
        return !hasRefunds() || isTotalGreaterThanZero(fndFunds, fndRefunds) || isTotalGreaterThanZero(otherFunds, otherRefunds);
    }

    private boolean isTotalGreaterThanZero(final TokenValueDto fundTokenValue, final TokenValueDto refundTokenValue) {
        return fundTokenValue != null && refundTokenValue != null && fundTokenValue.getTotalAmount().add(refundTokenValue.getTotalAmount()).compareTo(BigDecimal.ZERO) > 0;
    }
}
