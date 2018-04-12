package io.fundrequest.platform.profile.ref.dto;

import io.fundrequest.platform.profile.ref.domain.ReferralStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReferralDto {

    private LocalDateTime createdAt;
    private ReferralStatus status;
    private String email;
    private String name;
    private String picture;
}
