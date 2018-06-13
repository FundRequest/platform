package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.domain.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimableResultDto {
    private boolean claimable;
    private Platform platform;
    private String claimableByPlatformUserName;
}
