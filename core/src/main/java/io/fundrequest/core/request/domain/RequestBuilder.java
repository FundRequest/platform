package io.fundrequest.core.request.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class RequestBuilder {
    private IssueInformation issueInformation;
    private List<String> watchers = new ArrayList<>();
    private Set<RequestTechnology> technologies = new HashSet<>();
    private Long id;
    private RequestStatus status;

    private RequestBuilder() {
    }

    public static RequestBuilder aRequest() {
        return new RequestBuilder();
    }

    public RequestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RequestBuilder withIssueInformation(IssueInformation issueInformation) {
        this.issueInformation = issueInformation;
        return this;
    }

    public RequestBuilder withWatchers(List<String> watchers) {
        this.watchers = watchers;
        return this;
    }

    public RequestBuilder withTechnologies(Set<RequestTechnology> technologies) {
        this.technologies = technologies;
        return this;
    }

    public RequestBuilder withStatus(RequestStatus status) {
        this.status = status;
        return this;
    }

    public RequestBuilder but() {
        return aRequest().withWatchers(watchers)
                         .withTechnologies(technologies)
                         .withStatus(status)
                         .withIssueInformation(issueInformation);
    }

    public Request build() {
        Request request = new Request();
        request.setId(id);
        request.setIssueInformation(issueInformation);
        if (status != null) {
            request.setStatus(status);
        }
        watchers.forEach(request::addWatcher);
        technologies.forEach(request::addTechnology);
        return request;
    }
}
