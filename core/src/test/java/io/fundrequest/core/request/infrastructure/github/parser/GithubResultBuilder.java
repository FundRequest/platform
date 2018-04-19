package io.fundrequest.core.request.infrastructure.github.parser;

import io.fundrequest.platform.github.parser.GithubResult;

public final class GithubResultBuilder {
    private String id;
    private String number;
    private String title;
    private String state;

    private GithubResultBuilder() {
    }

    public static GithubResultBuilder aGithubResult() {
        return new GithubResultBuilder();
    }


    public GithubResultBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public GithubResultBuilder withNumber(String number) {
        this.number = number;
        return this;
    }

    public GithubResultBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GithubResultBuilder withState(String state) {
        this.state = state;
        return this;
    }

    public GithubResultBuilder but() {
        return aGithubResult().withId(id).withNumber(number).withTitle(title).withState(state);
    }

    public GithubResult build() {
        GithubResult githubResult = new GithubResult();
        githubResult.setId(id);
        githubResult.setNumber(number);
        githubResult.setTitle(title);
        githubResult.setState(state);
        return githubResult;
    }
}
