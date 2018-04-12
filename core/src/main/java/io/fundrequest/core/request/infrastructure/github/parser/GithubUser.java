package io.fundrequest.core.request.infrastructure.github.parser;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GithubUser {
    private Long id;
    private String login;
    private LocalDateTime createdAt;
    private String location;

    GithubUser() {
    }

    @Builder
    public GithubUser(Long id, String login, LocalDateTime createdAt, String location) {
        this.id = id;
        this.login = login;
        this.createdAt = createdAt;
        this.location = location;
    }
}
