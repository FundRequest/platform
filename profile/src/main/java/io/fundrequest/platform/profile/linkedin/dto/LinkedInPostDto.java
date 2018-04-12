package io.fundrequest.platform.profile.linkedin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkedInPostDto {
    private Long id;

    private String title;

    private String description;

    private String submittedUrl;

    private String submittedImageUrl;

    private String comment;
}
