package io.fundrequest.platform.github.scraper.model;


import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubIdTest {

    @Test
    public void fromString_issue() {

        final Optional<GithubId> resultOptional = GithubId.fromString("/FundRequest/area51/issues/38");

        assertThat(resultOptional).isPresent();
        final GithubId result = resultOptional.get();
        assertThat(result.getOwner()).isEqualTo("FundRequest");
        assertThat(result.getRepo()).isEqualTo("area51");
        assertThat(result.getNumber()).isEqualTo("38");
    }

    @Test
    public void fromString_issueFullURL() {

        final Optional<GithubId> resultOptional = GithubId.fromString("https://github.com/trufflesuite/truffle/issues/501");

        assertThat(resultOptional).isPresent();
        final GithubId result = resultOptional.get();
        assertThat(result.getOwner()).isEqualTo("trufflesuite");
        assertThat(result.getRepo()).isEqualTo("truffle");
        assertThat(result.getNumber()).isEqualTo("501");
    }

    @Test
    public void fromString_pullrequest() {

        final Optional<GithubId> resultOptional = GithubId.fromString("/smartcontractkit/chainlink/pull/276");

        assertThat(resultOptional).isPresent();
        final GithubId result = resultOptional.get();
        assertThat(result.getOwner()).isEqualTo("smartcontractkit");
        assertThat(result.getRepo()).isEqualTo("chainlink");
        assertThat(result.getNumber()).isEqualTo("276");
    }

    @Test
    public void fromString_pullrequestFullURL() {

        final Optional<GithubId> resultOptional = GithubId.fromString("https://github.com/aragon/aragonOS/pull/204");

        assertThat(resultOptional).isPresent();
        final GithubId result = resultOptional.get();
        assertThat(result.getOwner()).isEqualTo("aragon");
        assertThat(result.getRepo()).isEqualTo("aragonOS");
        assertThat(result.getNumber()).isEqualTo("204");
    }

    @Test
    public void noMatchEmtpy() {

        final Optional<GithubId> resultOptional = GithubId.fromString("/hfgdjfgk");

        assertThat(resultOptional).isEmpty();
    }
}
