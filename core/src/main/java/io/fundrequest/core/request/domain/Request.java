package io.fundrequest.core.request.domain;

import io.fundrequest.core.infrastructure.repository.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "request")
@Entity
public class Request extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_link")
    private String issueLink;

    @Column(name = "label")
    private String label;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private RequestStatus status = RequestStatus.OPEN;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private RequestType type = RequestType.ISSUE;

    @Column(name = "source")
    @Enumerated(value = EnumType.STRING)
    private RequestSource source = RequestSource.GITHUB;

    Request() {
    }

    public void setIssueLink(String issueLink) {
        this.issueLink = issueLink;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public String getIssueLink() {
        return issueLink;
    }

    public String getLabel() {
        return label;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public RequestType getType() {
        return type;
    }

    public RequestSource getSource() {
        return source;
    }
}
