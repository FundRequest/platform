package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FundsAndRefundsAggregatorTest {

    private FundsAndRefundsAggregator fundsAndRefundsAggregator;

    @BeforeEach
    public void setUp() {
        fundsAndRefundsAggregator = new FundsAndRefundsAggregator();
    }

    @Test
    public void aggregate() {

        final String funder1UserId = "dgfhj";
        final String funder1Address = "0xFHDsad";
        final String funder2UserId = "jghf";
        final String funder2Address = "0xtrdkl";
        final String funder3UserId = "hfg";
        final String funder3Address = "0xeytru";
        final FundsByFunderDto fundsByFunderDto1 = buildFundsByFunderDto(funder1UserId, funder1Address, "10", "20");
        final FundsByFunderDto fundsByFunderDto2 = buildFundsByFunderDto(funder2UserId, funder2Address, null, "-30");
        final FundsByFunderDto fundsByFunderDto3 = buildFundsByFunderDto(funder3UserId, funder3Address, "65", null);
        final FundsByFunderDto fundsByFunderDto4 = buildFundsByFunderDto(funder1UserId, funder1Address.toUpperCase(), "-10", "-10");
        final FundsByFunderDto fundsByFunderDto5 = buildFundsByFunderDto(funder2UserId, funder2Address, null, "60");
        final FundsByFunderDto fundsByFunderDto6 = buildFundsByFunderDto(funder3UserId, funder3Address, "-35", null);

        final List<UserFundsDto> result = fundsAndRefundsAggregator.aggregate(Arrays.asList(fundsByFunderDto1, fundsByFunderDto2, fundsByFunderDto3, fundsByFunderDto4, fundsByFunderDto5, fundsByFunderDto6));

        assertThat(result).contains(buildUserFundsDtoFrom(fundsByFunderDto1, fundsByFunderDto4),
                                    buildUserFundsDtoFrom(fundsByFunderDto5, fundsByFunderDto2),
                                    buildUserFundsDtoFrom(fundsByFunderDto3, fundsByFunderDto6));
    }

    private FundsByFunderDto buildFundsByFunderDto(final String funder1UserId, final String funder1Address, final String fndValue, final String zrxValue) {
        return FundsByFunderDto.builder()
                               .funderUserId(funder1UserId)
                               .funderAddress(funder1Address)
                               .fndValue(fndValue != null ? TokenValueDtoMother.FND().totalAmount(new BigDecimal(fndValue)).build() : null)
                               .otherValue(zrxValue != null ? TokenValueDtoMother.ZRX().totalAmount(new BigDecimal(zrxValue)).build() : null)
                               .build();
    }

    private UserFundsDto buildUserFundsDtoFrom(final FundsByFunderDto funds, final FundsByFunderDto refunds) {
        return UserFundsDto.builder()
                           .funderUserId(funds.getFunderUserId())
                           .funderAddress(funds.getFunderAddress())
                           .fndFunds(funds.getFndValue())
                           .otherFunds(funds.getOtherValue())
                           .fndRefunds(refunds.getFndValue())
                           .otherRefunds(refunds.getOtherValue())
                           .build();
    }
}
