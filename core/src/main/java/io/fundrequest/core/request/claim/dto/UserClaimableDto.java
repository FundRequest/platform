package io.fundrequest.core.request.claim.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserClaimableDto {
    private final boolean claimable;
    private final boolean claimableByUser;
}
