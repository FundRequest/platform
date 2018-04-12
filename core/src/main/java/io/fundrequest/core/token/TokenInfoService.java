package io.fundrequest.core.token;

import io.fundrequest.core.token.dto.TokenInfoDto;

public interface TokenInfoService {

    TokenInfoDto getTokenInfo(String tokenAddress);
}
