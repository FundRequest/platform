package io.fundrequest.platform.github.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
public class GithubRateLimit {
    private int limit;
    private int remaining;
    private long reset;
}
