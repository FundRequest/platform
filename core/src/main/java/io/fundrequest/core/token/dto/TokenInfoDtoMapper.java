package io.fundrequest.core.token.dto;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.token.domain.TokenInfo;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMappingConfig.class)
public interface TokenInfoDtoMapper extends BaseMapper<TokenInfo, TokenInfoDto> {
}
