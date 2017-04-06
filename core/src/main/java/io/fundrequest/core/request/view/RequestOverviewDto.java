package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.RequestSource;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestType;

public class RequestOverviewDto {
    private Integer id;

    private String issueLink;

    private String label;

    private RequestStatus status;

    private RequestType type;

    private RequestSource source;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIssueLink() {
        return issueLink;
    }

    public void setIssueLink(String issueLink) {
        this.issueLink = issueLink;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public RequestSource getSource() {
        return source;
    }

    public void setSource(RequestSource source) {
        this.source = source;
    }
}
