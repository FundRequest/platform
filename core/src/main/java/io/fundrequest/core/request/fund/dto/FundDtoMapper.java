package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMappingConfig.class,
        uses = {TokenValueDtoMapper.class})
public interface FundDtoMapper extends BaseMapper<Fund, FundDto> {
}