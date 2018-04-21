package io.fundrequest.platform.profile.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileProvider {
    private String username;
    private String userId;
}
