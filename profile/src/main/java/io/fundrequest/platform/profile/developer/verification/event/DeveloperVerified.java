package io.fundrequest.platform.profile.developer.verification.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeveloperVerified {
    private String userId;
}
