package io.fundrequest.platform.profile.profile.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GithubVerificationDto {
    private final LocalDateTime createdAt;
    private final boolean approved;
}
