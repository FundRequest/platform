package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestClaimDto {
    private Long id;
    private String address;
    private String solver;
    private ClaimRequestStatus status = ClaimRequestStatus.PENDING;
    private Boolean flagged;
//    private String platform;
//    private String link;

}
