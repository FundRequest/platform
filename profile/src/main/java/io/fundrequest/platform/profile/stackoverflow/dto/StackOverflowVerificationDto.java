package io.fundrequest.platform.profile.stackoverflow.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StackOverflowVerificationDto {
    private final LocalDateTime createdAt;
    private final boolean approved;
}
