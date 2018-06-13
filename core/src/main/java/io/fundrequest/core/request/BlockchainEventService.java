package io.fundrequest.core.request;

import io.fundrequest.core.request.dto.BlockchainEventDto;

import java.util.Optional;

public interface BlockchainEventService {

    Optional<BlockchainEventDto> findOne(Long id);
}
