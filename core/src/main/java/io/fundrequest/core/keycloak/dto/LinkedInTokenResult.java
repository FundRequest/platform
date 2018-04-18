package io.fundrequest.core.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LinkedInTokenResult {

    @JsonProperty("access_token")
    private String accessToken;
}
