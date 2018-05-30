package io.fundrequest.core.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainEventDto {

    private Long id;
    private String transactionHash;
    private String logIndex;
    private LocalDateTime processDate;

}
