package io.fundrequest.core.request.claim.event;

import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.view.RequestDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestClaimedEvent {
    private final Long blockchainEventId;
    private final RequestDto requestDto;
    private final ClaimDto claimDto;
    private final String solver;
    private final LocalDateTime timestamp;
}
