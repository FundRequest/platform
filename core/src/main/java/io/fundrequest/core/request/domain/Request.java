package io.fundrequest.core.request.domain;

import io.fundrequest.core.infrastructure.repository.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
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

    @Embedded
    private IssueInformation issueInformation;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private RequestStatus status = RequestStatus.OPEN;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private RequestType type = RequestType.ISSUE;

    Request() {
    }


    public Long getId() {
        return id;
    }


    public RequestStatus getStatus() {
        return status;
    }

    public RequestType getType() {
        return type;
    }

    public IssueInformation getIssueInformation() {
        return issueInformation;
    }

    public void setIssueInformation(IssueInformation issueInformation) {
        this.issueInformation = issueInformation;
    }
}
