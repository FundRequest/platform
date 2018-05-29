package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.claim.dto.ClaimByTransactionAggregate;
import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimsByTransactionAggregate;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.view.ClaimDtoMother;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.core.token.model.TokenValueMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static io.fundrequest.core.token.dto.TokenValueDtoMother.FND;
import static io.fundrequest.core.token.dto.TokenValueDtoMother.ZRX;
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
        final TokenValueDto fndValue1 = FND().totalAmount(new BigDecimal("50")).build();
        final TokenValueDto fndValue2 = FND().totalAmount(new BigDecimal("4.26")).build();
        final TokenValueDto zrxValue1 = ZRX().totalAmount(new BigDecimal("48")).build();
        final TokenValueDto zrxValue2 = ZRX().totalAmount(new BigDecimal("7")).build();
        final TokenValueDto fndTotal = TokenValueDtoMother.FND().totalAmount(new BigDecimal("54.26")).build();
        final TokenValueDto zrxTotal = TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("55")).build();
        final String txHash1 = "txHash1";
        final String txHash2 = "txHash2";
        final ClaimDto claim1 = ClaimDtoMother.aClaimDto().tokenValue(fndValue1).transactionHash(txHash1).solver(solver1).build();
        final ClaimDto claim2 = ClaimDtoMother.aClaimDto().tokenValue(fndValue2).transactionHash(txHash2).solver(solver2).build();
        final ClaimDto claim3 = ClaimDtoMother.aClaimDto().tokenValue(zrxValue1).transactionHash(txHash2).solver(solver1).build();
        final ClaimDto claim4 = ClaimDtoMother.aClaimDto().tokenValue(zrxValue2).transactionHash(txHash1).solver(solver1).build();
        final ClaimByTransactionAggregate transaction1 = ClaimByTransactionAggregate.builder()
                                                                                    .solver(claim1.getSolver())
                                                                                    .transactionHash(txHash1)
                                                                                    .timestamp(claim1.getTimestamp())
                                                                                    .fndValue(fndValue1)
                                                                                    .otherValue(zrxValue2)
                                                                                    .build();
        final ClaimByTransactionAggregate transaction2 = ClaimByTransactionAggregate.builder()
                                                                                    .solver(claim2.getSolver())
                                                                                    .transactionHash(txHash2)
                                                                                    .timestamp(claim2.getTimestamp())
                                                                                    .fndValue(fndValue2)
                                                                                    .otherValue(zrxValue1)
                                                                                    .build();
        final double expectedUSD = 3456.3D;

        when(fiatService.getUsdPrice(fndTotal, zrxTotal)).thenReturn(expectedUSD);

        final ClaimsByTransactionAggregate claims = claimDtoAggregator.aggregateClaims(Arrays.asList(claim1, claim2, claim3, claim4));

        assertThat(claims.getClaims()).isEqualTo(Arrays.asList(transaction1, transaction2));
        assertThat(claims.getFndValue()).isEqualTo(fndTotal);
        assertThat(claims.getOtherValue()).isEqualTo(zrxTotal);
        assertThat(claims.getUsdValue()).isEqualTo(expectedUSD);
    }


}
