package io.fundrequest.platform.profile.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class StackOverflowResult {

    @JsonProperty("items")
    private List<StackOverflowUser> users;

    StackOverflowResult() {
    }

    @Builder
    StackOverflowResult(List<StackOverflowUser> users) {
        this.users = users;
    }
}
