package io.fundrequest.core.request.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("request_id")
    private Long requestId;
    private String url;

    RequestClaimDto() {
    }

    @Builder
    public RequestClaimDto(Long id, String address, String solver, ClaimRequestStatus status, Boolean flagged, Long requestId, String url) {
        this.id = id;
        this.address = address;
        this.solver = solver;
        this.status = status;
        this.flagged = flagged;
        this.requestId = requestId;
        this.url = url;
    }
}
