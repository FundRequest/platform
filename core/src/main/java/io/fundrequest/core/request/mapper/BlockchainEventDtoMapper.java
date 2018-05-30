package io.fundrequest.core.request.mapper;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.core.infrastructure.mapping.DefaultMappingConfig;
import io.fundrequest.core.request.domain.BlockchainEvent;
import io.fundrequest.core.request.dto.BlockchainEventDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMappingConfig.class)
public interface BlockchainEventDtoMapper extends BaseMapper<BlockchainEvent, BlockchainEventDto> {
}
