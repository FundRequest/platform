package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class RequestClaimDto {
    private Long id;
    private String address;
    private String solver;
    private ClaimRequestStatus status = ClaimRequestStatus.PENDING;
    private Boolean flagged;
    private String url;


    RequestClaimDto() {
    }


    @Builder
    public RequestClaimDto(Long id, String address, String solver, ClaimRequestStatus status, Boolean flagged, String url) {
        this.id = id;
        this.address = address;
        this.solver = solver;
        this.status = status;
        this.flagged = flagged;
        this.url = url;
    }
}
