package io.fundrequest.platform.profile.linkedin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LinkedInShare {
    private String comment;
    private LinkedInShareVisibility visibility;
    private LinkedInShareContent content;

    LinkedInShare() {
    }

    @Builder
    public LinkedInShare(String comment, LinkedInShareContent content) {
        visibility = new LinkedInShareVisibility();
        this.comment = comment;
        this.content = content;
    }
}
