package io.fundrequest.platform.profile.bounty.dto;

import io.fundrequest.platform.profile.bounty.domain.BountyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaidBountyDto {
    private BountyType type;
    private int amount;
    private String transactionHash;
}
