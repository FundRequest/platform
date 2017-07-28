package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestType;

import java.util.Set;

public class RequestOverviewDto {
    private Long id;

    private RequestStatus status;

    private RequestType type;

    private IssueInformationDto issueInformation;

    private Set<String> technologies;

    private boolean loggedInUserIsWatcher;

    public IssueInformationDto getIssueInformation() {
        return issueInformation;
    }

    public void setIssueInformation(IssueInformationDto issueInformation) {
        this.issueInformation = issueInformation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Set<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<String> technologies) {
        this.technologies = technologies;
    }

    public boolean isLoggedInUserIsWatcher() {
        return loggedInUserIsWatcher;
    }

    public void setLoggedInUserIsWatcher(boolean loggedInUserIsWatcher) {
        this.loggedInUserIsWatcher = loggedInUserIsWatcher;
    }
}
