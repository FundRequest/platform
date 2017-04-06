package io.fundrequest.core.request.domain;

public final class RequestBuilder {
    private String issueLink;
    private String label;

    private RequestBuilder() {
    }

    public static RequestBuilder aRequest() {
        return new RequestBuilder();
    }

    public RequestBuilder withIssueLink(String issueLink) {
        this.issueLink = issueLink;
        return this;
    }

    public RequestBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public RequestBuilder but() {
        return aRequest().withIssueLink(issueLink).withLabel(label);
    }

    public Request build() {
        Request request = new Request();
        request.setIssueLink(issueLink);
        request.setLabel(label);
        return request;
    }
}
