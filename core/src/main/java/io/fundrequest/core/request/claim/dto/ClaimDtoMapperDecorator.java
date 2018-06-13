package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.BlockchainEventService;
import io.fundrequest.core.request.claim.domain.Claim;
import io.fundrequest.core.request.dto.BlockchainEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ClaimDtoMapperDecorator implements ClaimDtoMapper {

    @Autowired
    @Qualifier("delegate")
    private ClaimDtoMapper delegate;

    @Autowired
    private BlockchainEventService blockchainEventService;

    @Override
    public ClaimDto map(Claim r) {
        final ClaimDto dto = delegate.map(r);
        if (dto != null) {
            dto.setTransactionHash(blockchainEventService.findOne(r.getBlockchainEventId())
                                                         .map(BlockchainEventDto::getTransactionHash)
                                                         .orElse(""));
        }
        return dto;
    }
}
