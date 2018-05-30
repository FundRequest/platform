package io.fundrequest.core.request.fund.event;

import io.fundrequest.core.request.fund.dto.FundDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestFundedEvent {
    private final FundDto fundDto;
    private final Long requestId;
    private final LocalDateTime timestamp;
}
