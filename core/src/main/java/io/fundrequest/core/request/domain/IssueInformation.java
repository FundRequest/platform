package io.fundrequest.core.request.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class IssueInformation {

    @Column(name = "issue_link")
    private String link;

    @Column(name = "owner")
    private String owner;

    @Column(name = "repo")
    private String repo;

    @Column(name = "issue_number")
    private String number;

    @Column(name = "title")
    private String title;

    @Column(name = "source")
    @Enumerated(value = EnumType.STRING)
    private RequestSource source;

    public String getLink() {
        return link;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepo() {
        return repo;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RequestSource getSource() {
        return source;
    }

    public void setSource(RequestSource source) {
        this.source = source;
    }
}
