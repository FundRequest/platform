package io.fundrequest.core.request.fund;

import io.fundrequest.core.token.dto.TokenValueDto;
import org.junit.jupiter.api.Test;

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
}