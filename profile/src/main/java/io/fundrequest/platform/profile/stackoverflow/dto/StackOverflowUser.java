package io.fundrequest.platform.profile.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class StackOverflowUser {

    StackOverflowUser() {
    }

    @JsonProperty("age")
    private Long age;

    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("reputation")
    private Long reputation;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("creation_date")
    private Long createdAt;

    @JsonProperty("user_id")
    private String userId;

    public LocalDateTime getCreationDateTime() {
        return createdAt == null
               ? null
               : LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt * 1000), ZoneId.systemDefault());
    }
}
