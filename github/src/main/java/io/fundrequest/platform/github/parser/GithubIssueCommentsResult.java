package io.fundrequest.platform.github.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubIssueCommentsResult {
    private Long id;
    private GithubUser user;
    private String title;
    private ZonedDateTime createdAt;
    private String body;
}
