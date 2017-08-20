package io.fundrequest.core.request.domain;

import io.fundrequest.core.infrastructure.repository.AbstractEntity;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "request_watcher",
            joinColumns = @JoinColumn(name = "request_id")
    )
    @Column(name = "email")
    private Set<String> watchers = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "request_technology",
            joinColumns = @JoinColumn(name = "request_id")
    )
    @Column(name = "technology")
    private Set<String> technologies = new HashSet<>();

    protected Request() {
    }


    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
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

    public void addWatcher(String email) {
        this.watchers.add(email);
    }

    public void removeWatcher(String email) {
        this.watchers.remove(email);
    }

    public Set<String> getWatchers() {
        return Collections.unmodifiableSet(watchers);
    }

    public void addTechnology(String technology) {
        this.technologies.add(technology);
    }

    public void removeTechnology(String technology) {
        this.technologies.remove(technology);
    }

    public Set<String> getTechnologies() {
        return Collections.unmodifiableSet(technologies);
    }
}
