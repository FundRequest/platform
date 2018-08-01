package io.fundrequest.core.request.domain;

import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.token.dto.TokenInfoDtoMother;
import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.core.token.model.TokenValueMother;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class FundMother {

    public static Fund.FundBuilder fndFundFunderKnown() {
        return fndFundFunderKnown("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853", "e7356d6a-4eff-4003-8736-557c36ce6e0c", "3870000000000000000");
    }

    public static Fund.FundBuilder zrxFundFunderKnown() {
        return zrxFundFunderKnown("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853", "e7356d6a-4eff-4003-8736-557c36ce6e0c", "38700000276700000000000000000000000000");
    }

    public static Fund.FundBuilder fndFundFunderNotKnown() {
        return fndFundFunderNotKnown("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853", "4870000000000000000");
    }

    public static Fund.FundBuilder zrxFundFunderNotKnown() {
        return zrxFundFunderNotKnown("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853", "1767000000000000000");
    }

    public static Fund.FundBuilder fndFundFunderKnown(final String funderAddress, final String funderUserId, final String amount) {
        return Fund.builder()
                   .tokenValue(TokenValueMother.FND().amountInWei(new BigDecimal(amount)).build())
                   .funderUserId(funderUserId)
                   .funderAddress(funderAddress)
                   .timestamp(LocalDateTime.now())
                   .requestId(1L);
    }

    public static Fund.FundBuilder zrxFundFunderKnown(final String funderAddress, final String funderUserId, final String amount) {
        return Fund.builder()
                   .tokenValue(TokenValueMother.ZRX().amountInWei(new BigDecimal(amount)).build())
                   .funderUserId(funderUserId)
                   .funderAddress(funderAddress)
                   .timestamp(LocalDateTime.now())
                   .requestId(1L);
    }

    public static Fund.FundBuilder fndFundFunderNotKnown(final String funderAddress, final String amount) {
        return Fund.builder()
                   .tokenValue(TokenValueMother.FND().amountInWei(new BigDecimal(amount)).build())
                   .funderAddress(funderAddress)
                   .timestamp(LocalDateTime.now())
                   .requestId(1L);
    }

    public static Fund.FundBuilder zrxFundFunderNotKnown(final String funderAddress, final String amount) {
        return Fund.builder()
                   .tokenValue(TokenValueMother.ZRX().amountInWei(new BigDecimal(amount)).build())
                   .funderAddress(funderAddress)
                   .timestamp(LocalDateTime.now())
                   .requestId(1L);
    }
}
