package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequestDto extends AbstractEntity {

    private Long id;
    private Long requestId;
    private String funderAddress;
    private RefundRequestStatus status = RefundRequestStatus.PENDING;
    private String transactionHash;
    private LocalDateTime transactionSubmitTime;

}
