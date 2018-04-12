package io.fundrequest.platform.profile.linkedin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LinkedInShareContent {

    private String title;

    private String description;

    @JsonProperty("submitted-url")
    private String submittedUrl;

    @JsonProperty("submitted-image-url")
    private String submittedImageUrl;

    LinkedInShareContent() {
    }

    @Builder
    public LinkedInShareContent(String title, String description, String submittedUrl, String submittedImageUrl) {
        this.title = title;
        this.description = description;
        this.submittedUrl = submittedUrl;
        this.submittedImageUrl = submittedImageUrl;
    }
}
