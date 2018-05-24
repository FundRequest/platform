package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimsAggregate;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.view.ClaimDtoMother;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.core.token.model.TokenValueMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClaimDtoAggregatorTest {

    private ClaimDtoAggregator claimDtoAggregator;

    private String fndContractorAddress;
    private FiatService fiatService;

    @BeforeEach
    public void setUp() {
        fndContractorAddress = TokenValueMother.FND().build().getTokenAddress();
        fiatService = mock(FiatService.class);
        claimDtoAggregator = new ClaimDtoAggregator(fndContractorAddress, fiatService);
    }

    @Test
    void aggregateClaims() {
        final String solver1 = "dgh";
        final String solver2 = "hdxhg";
        final TokenValueDto.TokenValueDtoBuilder fndValueBuilder = TokenValueDtoMother.FND();
        final TokenValueDto.TokenValueDtoBuilder zrxValueBuilder = TokenValueDtoMother.ZRX();
        final TokenValueDto fndValue1 = fndValueBuilder.totalAmount(new BigDecimal("50")).build();
        final TokenValueDto fndValue2 = fndValueBuilder.totalAmount(new BigDecimal("4.26")).build();
        final TokenValueDto zrxValue1 = zrxValueBuilder.totalAmount(new BigDecimal("48")).build();
        final TokenValueDto fndTotal = TokenValueDto.builder()
                                                    .tokenSymbol("FND")
                                                    .tokenAddress(fndContractorAddress)
                                                    .totalAmount(fndValue1.getTotalAmount().add(fndValue2.getTotalAmount()))
                                                    .build();
        final List<ClaimDto> claimsList = Arrays.asList(ClaimDtoMother.aClaimDto().tokenValue(fndValue1).solver(solver1).build(),
                                                        ClaimDtoMother.aClaimDto().tokenValue(fndValue2).solver(solver2).build(),
                                                        ClaimDtoMother.aClaimDto().tokenValue(zrxValue1).solver(solver1).build());
        final double expectedUSD = 3456.3D;

        when(fiatService.getUsdPrice(fndTotal, zrxValue1)).thenReturn(expectedUSD);

        final ClaimsAggregate claims = claimDtoAggregator.aggregateClaims(claimsList);

        assertThat(claims.getClaims()).isSameAs(claimsList);
        assertThat(claims.getFndValue()).isEqualTo(fndTotal);
        assertThat(claims.getOtherValue()).isEqualTo(zrxValue1);
        assertThat(claims.getUsdValue()).isEqualTo(expectedUSD);
    }
}
