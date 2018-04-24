package io.fundrequest.platform.github.parser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "with")
public class GithubRateLimit {
    private int limit;
    private int remaining;
    private long reset;
}
