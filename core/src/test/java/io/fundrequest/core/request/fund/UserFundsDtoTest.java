package io.fundrequest.core.request.fund;

import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class UserFundsDtoTest {

    @Test
    void hasRefunds_noRefunds() {
        assertThat(UserFundsDto.builder().build().hasRefunds()).isFalse();
    }
    @Test
    void hasRefunds_fndRefunds() {
        assertThat(UserFundsDto.builder().fndRefunds(TokenValueDto.builder().build()).build().hasRefunds()).isTrue();
    }

    @Test
    void hasRefunds_otherRefunds() {
        assertThat(UserFundsDto.builder().otherRefunds(TokenValueDto.builder().build()).build().hasRefunds()).isTrue();
    }

    @Test
    void hasRefunds_fndAndOtherRefunds() {
        assertThat(UserFundsDto.builder().fndRefunds(TokenValueDto.builder().build()).otherRefunds(TokenValueDto.builder().build()).build().hasRefunds()).isTrue();
    }

    @Test
    public void isRefundable_fundsAndNoRefunds() {
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .build()
                               .isRefundable()).isTrue();
        assertThat(UserFundsDto.builder()
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .build()
                               .isRefundable()).isTrue();
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .build()
                               .isRefundable()).isTrue();
    }

    @Test
    public void isRefundable_fundsAndRefundsGtZero() {
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-10")).build())
                               .build()
                               .isRefundable()).isTrue();
        assertThat(UserFundsDto.builder()
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .otherRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-10")).build())
                               .build()
                               .isRefundable()).isTrue();
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-10")).build())
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .otherRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-10")).build())
                               .build()
                               .isRefundable()).isTrue();
    }

    @Test
    public void isRefundable_fundsAndRefundsAllEqZero() {
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-20")).build())
                               .build()
                               .isRefundable()).isFalse();
        assertThat(UserFundsDto.builder()
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .otherRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-20")).build())
                               .build()
                               .isRefundable()).isFalse();
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-20")).build())
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .otherRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-20")).build())
                               .build()
                               .isRefundable()).isFalse();
    }

    @Test
    public void isRefundable_fundsAndRefundsOneGtZero() {
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-20")).build())
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .otherRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-10")).build())
                               .build()
                               .isRefundable()).isTrue();
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-10")).build())
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .otherRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-30")).build())
                               .build()
                               .isRefundable()).isTrue();
    }

    @Test
    public void isRefundable_fundsAndRefundsAllLtZero() {
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-30")).build())
                               .build()
                               .isRefundable()).isFalse();
        assertThat(UserFundsDto.builder()
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .otherRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-30")).build())
                               .build()
                               .isRefundable()).isFalse();
        assertThat(UserFundsDto.builder()
                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("20")).build())
                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-30")).build())
                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("20")).build())
                               .otherRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-30")).build())
                               .build()
                               .isRefundable()).isFalse();
    }
}