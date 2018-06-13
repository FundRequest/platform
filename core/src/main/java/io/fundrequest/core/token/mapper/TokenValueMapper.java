package io.fundrequest.core.token.mapper;

import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static io.fundrequest.core.web3j.EthUtil.fromWei;

@Component
public class TokenValueMapper {

    private final TokenInfoService tokenInfoService;

    public TokenValueMapper(final TokenInfoService tokenInfoService) {
        this.tokenInfoService = tokenInfoService;
    }

    public TokenValueDto map(final String tokenAddress, final BigDecimal rawBalance) {
        final TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(tokenAddress);
        return tokenInfo == null ? null : TokenValueDto.builder()
                                                       .tokenAddress(tokenInfo.getAddress())
                                                       .tokenSymbol(tokenInfo.getSymbol())
                                                       .totalAmount(fromWei(rawBalance, tokenInfo.getDecimals()))
                                                       .build();
    }
}
