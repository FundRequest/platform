package io.fundrequest.core.token;

import io.fundrequest.core.erc20.service.ERC20Service;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenInfoServiceImpl implements TokenInfoService {


    private ERC20Service erc20Service;

    public TokenInfoServiceImpl(final ERC20Service erc20Service) {
        this.erc20Service = erc20Service;
    }

    @Override
    @Transactional(readOnly = true)
    public TokenInfoDto getTokenInfo(final String tokenAddress) {
        return TokenInfoDto.builder()
                           .address(tokenAddress)
                           .decimals(erc20Service.decimals(tokenAddress))
                           .name(erc20Service.name(tokenAddress))
                           .symbol(erc20Service.symbol(tokenAddress))
                           .build();
    }
}
