package io.fundrequest.core.token;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.token.domain.TokenInfo;
import io.fundrequest.core.token.dto.TokenInfoDto;
import io.fundrequest.core.token.infrastructure.TokenInfoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenInfoServiceImpl implements TokenInfoService {

    private TokenInfoRepository repository;
    private Mappers mappers;

    public TokenInfoServiceImpl(TokenInfoRepository repository, Mappers mappers) {
        this.repository = repository;
        this.mappers = mappers;
    }

    @Cacheable(value = "tokens", key = "#tokenAddress")
    @Override
    @Transactional(readOnly = true)
    public TokenInfoDto getTokenInfo(String tokenAddress) {
        return mappers.map(TokenInfo.class, TokenInfoDto.class, repository.getOne(tokenAddress));
    }
}
