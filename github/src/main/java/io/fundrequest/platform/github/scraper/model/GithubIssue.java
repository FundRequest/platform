package io.fundrequest.platform.github.scraper.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubIssue {

    private final String number;
    private final String status;
    private final String solver;

}
