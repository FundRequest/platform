package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class RequestClaimDto {
    private Long id;
    private String title;
    private String address;
    private String solver;
    private ClaimRequestStatus status = ClaimRequestStatus.PENDING;
    private Boolean flagged;
    private String url;
    private String fundRequestUrl;


    RequestClaimDto() {
    }


    @Builder
    public RequestClaimDto(Long id, String title, String address, String solver, ClaimRequestStatus status, Boolean flagged, String url, String fundRequestUrl) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.solver = solver;
        this.status = status;
        this.flagged = flagged;
        this.url = url;
        this.fundRequestUrl = fundRequestUrl;
    }
}
