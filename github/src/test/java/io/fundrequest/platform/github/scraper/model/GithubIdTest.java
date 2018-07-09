package io.fundrequest.platform.github.scraper.model;


import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubIdTest {

    @Test
    public void fromString_issue() {
        final String owner = "FundRequest";
        final String repo = "area51";
        final String number = "38";

        final Optional<GithubId> result = GithubId.fromString(String.format("/%s/%s/issues/%s", owner, repo, number));

        assertThat(result).isPresent()
                          .contains(GithubId.builder().owner(owner).repo(repo).number(number).build());
    }

    @Test
    public void fromString_issueFullURL() {
        final String owner = "trufflesuite";
        final String repo = "truffle";
        final String number = "501";

        final Optional<GithubId> result = GithubId.fromString(String.format("https://github.com/%s/%s/issues/%s", owner, repo, number));

        assertThat(result).isPresent()
                          .contains(GithubId.builder().owner(owner).repo(repo).number(number).build());
    }

    @Test
    public void fromString_pullrequest() {
        final String owner = "smartcontractkit";
        final String repo = "chainlink";
        final String number = "276";

        final Optional<GithubId> result = GithubId.fromString(String.format("/%s/%s/pull/%s", owner, repo, number));

        assertThat(result).isPresent()
                          .contains(GithubId.builder().owner(owner).repo(repo).number(number).build());
    }

    @Test
    public void fromString_pullrequestFullURL() {
        final String owner = "aragon";
        final String repo = "aragonOS";
        final String number = "204";

        final Optional<GithubId> result = GithubId.fromString(String.format("https://github.com/%s/%s/pull/%s", owner, repo, number));

        assertThat(result).isPresent()
                          .contains(GithubId.builder().owner(owner).repo(repo).number(number).build());
    }

    @Test
    public void fromString_noMatchEmtpy() {

        final Optional<GithubId> result = GithubId.fromString("/hfgdjfgk");

        assertThat(result).isEmpty();
    }

    @Test
    public void fromPlatformId() {
        final String owner = "fgagsfgfas";
        final String repo = "bdfdb";
        final String number = "213";

        final Optional<GithubId> result = GithubId.fromPlatformId(String.format("%s|FR|%s|FR|%s", owner, repo, number));

        assertThat(result).isPresent()
                          .contains(GithubId.builder().owner(owner).repo(repo).number(number).build());
    }

    @Test
    public void fromPlatformId_noMatchEmpty() {

        final Optional<GithubId> result = GithubId.fromPlatformId("fgagsfgfas|FR|bdfdb|FR");

        assertThat(result).isEmpty();
    }
}
