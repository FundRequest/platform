package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.BlockchainEventService;
import io.fundrequest.core.request.claim.domain.Claim;
import io.fundrequest.core.request.dto.BlockchainEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClaimDtoMapperDecoratorTest {

    private ClaimDtoMapperDecorator decorator;

    private ClaimDtoMapper delegate;
    private BlockchainEventService blockchainEventService;

    @BeforeEach
    void setUp() {
        delegate = mock(ClaimDtoMapper.class);
        blockchainEventService = mock(BlockchainEventService.class);

        decorator = new ClaimDtoMapperDecorator();
        ReflectionTestUtils.setField(decorator, "delegate", delegate);
        ReflectionTestUtils.setField(decorator, "blockchainEventService", blockchainEventService);
    }

    @Test
    void map() {
        final long blockchainEventId = 465L;
        final Claim claim = Claim.builder().blockchainEventId(blockchainEventId).build();
        final String transactionHash = "rqwerwet";

        when(delegate.map(claim)).thenReturn(new ClaimDto());
        when(blockchainEventService.findOne(blockchainEventId)).thenReturn(Optional.of(BlockchainEventDto.builder().transactionHash(transactionHash).build()));

        final ClaimDto result = decorator.map(claim);

        assertThat(result.getTransactionHash()).isEqualTo(transactionHash);
    }

    @Test
    void map_null() {
        when(delegate.map(null)).thenReturn(null);

        final ClaimDto result = decorator.map(null);

        assertThat(result).isNull();
    }

    @Test
    void map_blockchainEventEmpty() {
        final long blockchainEventId = 465L;
        final Claim claim = Claim.builder().blockchainEventId(blockchainEventId).build();

        when(delegate.map(claim)).thenReturn(new ClaimDto());
        when(blockchainEventService.findOne(blockchainEventId)).thenReturn(Optional.empty());

        final ClaimDto result = decorator.map(claim);

        assertThat(result.getTransactionHash()).isEmpty();
    }
}
