package io.fundrequest.core.request.domain;

import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.token.dto.TokenInfoDtoMother;
import io.fundrequest.core.token.model.TokenValue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class RefundMother {

    public static Refund.RefundBuilder fndRefundRequestedByKnown(final String requestedBy) {
        return fndRefundRequestedByNotKnown().requestedBy(requestedBy);
    }

    public static Refund.RefundBuilder fndRefundRequestedByNotKnown() {
        return Refund.builder()
                     .tokenValue(TokenValue.builder()
                                           .amountInWei(new BigDecimal("3870000000000000000"))
                                           .tokenAddress(TokenInfoDtoMother.fnd().getAddress())
                                           .build())
                     .funderAddress("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                     .creationDate(LocalDateTime.now())
                     .requestId(1L);
    }

    public static Refund.RefundBuilder zrxRefundRequestedByKnown() {
        return zrxRefundRequestedByNotKnown().requestedBy("e7356d6a-4eff-4003-8736-557c36ce6e0c");
    }

    public static Refund.RefundBuilder zrxRefundRequestedByNotKnown() {
        return Refund.builder()
                     .tokenValue(TokenValue.builder()
                                           .amountInWei(new BigDecimal("38700000276700000000000000000000000000"))
                                           .tokenAddress(TokenInfoDtoMother.zrx().getAddress())
                                           .build())
                     .funderAddress("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                     .creationDate(LocalDateTime.now())
                     .requestId(1L);
    }
}
