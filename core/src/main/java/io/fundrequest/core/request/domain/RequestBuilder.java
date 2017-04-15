package io.fundrequest.core.request.domain;

import java.util.ArrayList;
import java.util.List;

public final class RequestBuilder {
    private IssueInformation issueInformation;
    private List<String> watchers = new ArrayList<>();

    private RequestBuilder() {
    }

    public static RequestBuilder aRequest() {
        return new RequestBuilder();
    }

    public RequestBuilder withIssueInformation(IssueInformation issueInformation) {
        this.issueInformation = issueInformation;
        return this;
    }

    public RequestBuilder withWatchers(List<String> watchers) {
        this.watchers = watchers;
        return this;
    }

    public RequestBuilder but() {
        return aRequest().withWatchers(watchers)
                .withIssueInformation(issueInformation);
    }

    public Request build() {
        Request request = new Request();
        request.setIssueInformation(issueInformation);
        watchers.forEach(request::addWatcher);
        return request;
    }
}
