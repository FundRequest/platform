package io.fundrequest.core.request.domain;

public final class RequestBuilder {
    private IssueInformation issueInformation;

    private RequestBuilder() {
    }

    public static RequestBuilder aRequest() {
        return new RequestBuilder();
    }

    public RequestBuilder withIssueInformation(IssueInformation issueInformation) {
        this.issueInformation = issueInformation;
        return this;
    }

    public RequestBuilder but() {
        return aRequest().withIssueInformation(issueInformation);
    }

    public Request build() {
        Request request = new Request();
        request.setIssueInformation(issueInformation);
        return request;
    }
}
