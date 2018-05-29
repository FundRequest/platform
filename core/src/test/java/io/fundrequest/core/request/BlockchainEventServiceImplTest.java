package io.fundrequest.core.request;

import io.fundrequest.core.request.domain.BlockchainEvent;
import io.fundrequest.core.request.dto.BlockchainEventDto;
import io.fundrequest.core.request.infrastructure.BlockchainEventRepository;
import io.fundrequest.core.request.mapper.BlockchainEventDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlockchainEventServiceImplTest {

    private BlockchainEventService service;
    private BlockchainEventRepository blockchainEventRepo;
    private BlockchainEventDtoMapper mapper;

    @BeforeEach
    void setUp() {
        blockchainEventRepo = mock(BlockchainEventRepository.class);
        mapper = mock(BlockchainEventDtoMapper.class);
        service = new BlockchainEventServiceImpl(blockchainEventRepo, mapper);
    }

    @Test
    void findOne() {
        final long blockcheinEventId = 7546L;
        final BlockchainEvent blockchainEvent = mock(BlockchainEvent.class);
        final BlockchainEventDto blockchainEventDto = mock(BlockchainEventDto.class);

        when(blockchainEventRepo.findOne(blockcheinEventId)).thenReturn(Optional.of(blockchainEvent));
        when(mapper.mapToOptional(same(blockchainEvent))).thenReturn(Optional.of(blockchainEventDto));

        final Optional<BlockchainEventDto> result = service.findOne(blockcheinEventId);

        assertThat(result).containsSame(blockchainEventDto);
    }

    @Test
    void findOne_notFound() {
        final long blockcheinEventId = 7546L;

        when(blockchainEventRepo.findOne(blockcheinEventId)).thenReturn(Optional.empty());
        when(mapper.mapToOptional(null)).thenReturn(Optional.empty());

        final Optional<BlockchainEventDto> result = service.findOne(blockcheinEventId);

        assertThat(result).isEmpty();
    }
}