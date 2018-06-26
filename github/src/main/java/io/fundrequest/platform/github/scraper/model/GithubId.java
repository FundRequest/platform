package io.fundrequest.platform.github.scraper.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Builder
@EqualsAndHashCode
public class GithubId {

    private final String owner;
    private final String repo;
    private final String number;

    public static Optional<GithubId> fromString(final String githubIdAsString) {
        final Pattern pattern = Pattern.compile("^.*/(?<owner>.+)/(?<repo>.+)/.+/(?<number>\\d+)$");
        final Matcher matcher = pattern.matcher(githubIdAsString);
        if (matcher.matches()) {
            return Optional.of(GithubId.builder()
                                       .owner(matcher.group("owner"))
                                       .repo(matcher.group("repo"))
                                       .number(matcher.group("number"))
                                       .build());
        }
        return Optional.empty();
    }
}
