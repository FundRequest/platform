package io.fundrequest.core.token.mapper;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.model.TokenValue;
import org.springframework.stereotype.Component;

import static io.fundrequest.core.web3j.EthUtil.fromWei;

@Component
public class TokenValueDtoMapper implements BaseMapper<TokenValue, TokenValueDto> {

    private final TokenInfoService tokenInfoService;

    public TokenValueDtoMapper(final TokenInfoService tokenInfoService) {
        this.tokenInfoService = tokenInfoService;
    }

    @Override
    public TokenValueDto map(final TokenValue tokenValue) {
        final TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(tokenValue.getTokenAddress());
        return TokenValueDto.builder()
                            .tokenAddress(tokenValue.getTokenAddress())
                            .tokenSymbol(tokenInfo.getSymbol())
                            .totalAmount(fromWei(tokenValue.getAmountInWei(), tokenInfo.getDecimals()))
                            .build();
    }
}
