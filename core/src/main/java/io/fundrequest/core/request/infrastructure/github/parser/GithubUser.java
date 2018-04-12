package io.fundrequest.core.request.infrastructure.github.parser;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GithubUser {
    private Long id;
    private String login;
    private LocalDateTime createdAt;
    private String location;

}
