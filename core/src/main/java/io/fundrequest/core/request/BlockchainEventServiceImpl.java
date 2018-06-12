package io.fundrequest.core.request;

import io.fundrequest.core.request.dto.BlockchainEventDto;
import io.fundrequest.core.request.infrastructure.BlockchainEventRepository;
import io.fundrequest.core.request.mapper.BlockchainEventDtoMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlockchainEventServiceImpl implements BlockchainEventService {

    private final BlockchainEventRepository blockchainEventRepo;
    private final BlockchainEventDtoMapper mapper;

    public BlockchainEventServiceImpl(final BlockchainEventRepository blockchainEventRepo, final BlockchainEventDtoMapper mapper) {
        this.blockchainEventRepo = blockchainEventRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<BlockchainEventDto> findOne(final Long id) {
        return mapper.mapToOptional(blockchainEventRepo.findOne(id).orElse(null));
    }
}
