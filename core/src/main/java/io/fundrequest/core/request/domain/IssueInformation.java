package io.fundrequest.core.request.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class IssueInformation {

    @Column(name = "owner")
    private String owner;

    @Column(name = "repo")
    private String repo;

    @Column(name = "issue_number")
    private String number;

    @Column(name = "title")
    private String title;

    @Column(name = "platform")
    @Enumerated(value = EnumType.STRING)
    private Platform platform;

    @Column(name = "platform_id")
    private String platformId;

    public Platform getPlatform() {
        return platform;
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

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
}
