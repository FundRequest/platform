package io.fundrequest.platform.profile.linkedin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkedInVerificationDto {
    private Boolean verified;
    private String postUrl;
}
