package io.fundrequest.core.request.domain;

public final class IssueInformationBuilder {
    private String link;
    private String owner;
    private String repo;
    private String number;
    private String title;
    private RequestSource source;

    private IssueInformationBuilder() {
    }

    public static IssueInformationBuilder anIssueInformation() {
        return new IssueInformationBuilder();
    }

    public IssueInformationBuilder withLink(String link) {
        this.link = link;
        return this;
    }

    public IssueInformationBuilder withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public IssueInformationBuilder withRepo(String repo) {
        this.repo = repo;
        return this;
    }

    public IssueInformationBuilder withNumber(String number) {
        this.number = number;
        return this;
    }

    public IssueInformationBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public IssueInformationBuilder withSource(RequestSource source) {
        this.source = source;
        return this;
    }

    public IssueInformationBuilder but() {
        return anIssueInformation().withLink(link).withOwner(owner).withRepo(repo).withNumber(number).withTitle(title).withSource(source);
    }

    public IssueInformation build() {
        IssueInformation issueInformation = new IssueInformation();
        issueInformation.setLink(link);
        issueInformation.setOwner(owner);
        issueInformation.setRepo(repo);
        issueInformation.setNumber(number);
        issueInformation.setTitle(title);
        issueInformation.setSource(source);
        return issueInformation;
    }
}
