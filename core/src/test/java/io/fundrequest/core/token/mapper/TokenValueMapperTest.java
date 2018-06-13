package io.fundrequest.core.token.mapper;

import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.fundrequest.core.web3j.EthUtil.fromWei;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenValueMapperTest {

    private TokenValueMapper mapper;

    private TokenInfoService tokenInfoService;

    @BeforeEach
    void setUp() {
        tokenInfoService = mock(TokenInfoService.class);
        mapper = new TokenValueMapper(tokenInfoService);
    }

    @Test
    void map() {
        final String tokenAddress = "dafsd";
        final String symbol = "ZRX";
        final BigDecimal rawBalance = new BigDecimal("1000000000000000000");
        final int decimals = 18;
        final TokenInfoDto tokenInfoDto = TokenInfoDto.builder().address(tokenAddress).symbol(symbol).decimals(decimals).build();

        when(tokenInfoService.getTokenInfo(tokenAddress)).thenReturn(tokenInfoDto);

        final TokenValueDto result = mapper.map(tokenAddress, rawBalance);

        assertThat(result.getTokenAddress()).isEqualTo(tokenAddress);
        assertThat(result.getTokenSymbol()).isEqualTo(symbol);
        assertThat(result.getTotalAmount()).isEqualTo(fromWei(rawBalance, decimals));
    }

    @Test
    void map_tokenInfoNull() {
        final String tokenAddress = "dafsd";

        when(tokenInfoService.getTokenInfo(tokenAddress)).thenReturn(null);

        final TokenValueDto result = mapper.map(tokenAddress, new BigDecimal("1000000000000000000"));

        assertThat(result).isNull();
    }
}