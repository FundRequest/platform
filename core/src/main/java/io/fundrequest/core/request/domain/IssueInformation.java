package io.fundrequest.core.request.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
@Data
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

    protected IssueInformation() {
    }

    @Builder
    public IssueInformation(String owner, String repo, String number, String title, Platform platform, String platformId) {
        this.owner = owner;
        this.repo = repo;
        this.number = number;
        this.title = title;
        this.platform = platform;
        this.platformId = platformId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssueInformation that = (IssueInformation) o;
        return platform == that.platform &&
               Objects.equals(platformId, that.platformId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(platform, platformId);
    }
}
