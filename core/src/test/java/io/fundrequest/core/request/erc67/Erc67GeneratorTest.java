package io.fundrequest.core.request.erc67;

import io.fundrequest.core.request.fund.domain.CreateERC67FundRequest;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class Erc67GeneratorTest {

    private Erc67Generator erc67Generator;
    private TokenInfoService tokenInfoService;

    @BeforeEach
    public void setUp() {
        tokenInfoService = mock(TokenInfoService.class);
        erc67Generator = new Erc67Generator(tokenInfoService, "0xC16a102813B7bD98b0BEF2dF28FFCaf1Fbee97c0");
    }

    @Test
    public void toFunction() {
        CreateERC67FundRequest erc67FundRequest = CreateERC67FundRequest
                .builder()
                .amount(new BigDecimal("100.837483"))
                .tokenAddress("0x02F96eF85cAd6639500CA1cc8356F0b5CA5bF1D2")
                .platform("github")
                .platformId("1")
                .build();
        when(tokenInfoService.getTokenInfo(erc67FundRequest.getTokenAddress())).thenReturn(TokenInfoDto.builder().decimals(18).build());

        String result = erc67Generator.toFunction(erc67FundRequest);

        assertThat(result).isEqualTo("approveAndCall(address 0xC16a102813B7bD98b0BEF2dF28FFCaf1Fbee97c0, uint256 100837483000000000000, bytes 0x6769746875627c4141437c31)");
    }

}