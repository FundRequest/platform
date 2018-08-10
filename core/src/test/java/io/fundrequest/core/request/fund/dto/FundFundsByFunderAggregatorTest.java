package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import io.fundrequest.core.token.model.TokenValueMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FundFundsByFunderAggregatorTest {

    private FundFundsByFunderAggregator aggregator;
    private TokenValueDtoMapper tokenValueDtoMapper;
    private String fndTokenHash;

    @BeforeEach
    public void setUp() {
        tokenValueDtoMapper = mock(TokenValueDtoMapper.class);
        fndTokenHash = TokenValueDtoMother.FND().build().getTokenAddress();
        aggregator = new FundFundsByFunderAggregator(fndTokenHash, tokenValueDtoMapper);
    }

    @Test
    public void aggregate() {
        final String funderAddress1 = "0x3225";
        final String funderAddress2 = "0x3334";
        final String funderUserId1 = "srtgb";
        final String funderUserId2 = "badsj";
        final List<Fund> funds = Arrays.asList(FundMother.fndFundFunderKnown(funderAddress1, funderUserId1, "10").build(),
                                               FundMother.zrxFundFunderKnown(funderAddress1, funderUserId1, "20").build(),
                                               FundMother.zrxFundFunderKnown(funderAddress1, funderUserId1, "30").build(),
                                               FundMother.fndFundFunderKnown(funderAddress2, funderUserId2, "40").build(),
                                               FundMother.fndFundFunderKnown(funderAddress1, funderUserId1, "50").build());

        when(tokenValueDtoMapper.map(TokenValueMother.FND().amountInWei(new BigDecimal("10")).build())).thenReturn(TokenValueDtoMother.FND().totalAmount(new BigDecimal("10")).build());
        when(tokenValueDtoMapper.map(TokenValueMother.ZRX().amountInWei(new BigDecimal("20")).build())).thenReturn(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build());
        when(tokenValueDtoMapper.map(TokenValueMother.ZRX().amountInWei(new BigDecimal("30")).build())).thenReturn(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("30")).build());
        when(tokenValueDtoMapper.map(TokenValueMother.FND().amountInWei(new BigDecimal("40")).build())).thenReturn(TokenValueDtoMother.FND().totalAmount(new BigDecimal("40")).build());
        when(tokenValueDtoMapper.map(TokenValueMother.FND().amountInWei(new BigDecimal("50")).build())).thenReturn(TokenValueDtoMother.FND().totalAmount(new BigDecimal("50")).build());

        final List<FundsByFunderDto> result = aggregator.aggregate(funds);

        assertThat(result).contains(FundsByFunderDto.builder()
                                                    .funderUserId(funderUserId1)
                                                    .funderAddress(funderAddress1)
                                                    .fndValue(TokenValueDtoMother.FND().totalAmount(new BigDecimal("60")).build())
                                                    .otherValue(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("50")).build())
                                                    .build(),
                                    FundsByFunderDto.builder()
                                                    .funderUserId(funderUserId2)
                                                    .funderAddress(funderAddress2)
                                                    .fndValue(TokenValueDtoMother.FND().totalAmount(new BigDecimal("40")).build())
                                                    .build());

    }
}