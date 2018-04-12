package io.fundrequest.core.keycloak;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserIdentity {
    private Provider provider;
    private String username;
    private String userId;

}