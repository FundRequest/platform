package io.fundrequest.core.request;

import io.fundrequest.core.request.validation.GithubLink;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class CreateRequestCommand {

    @NotBlank
    @Length(min = 20, max = 2000)
    @GithubLink
    private String issueLink;

    private Set<String> technologies = new HashSet<>();

    public String getIssueLink() {
        return issueLink;
    }

    public void setIssueLink(String issueLink) {
        this.issueLink = issueLink;
    }

    public Set<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<String> technologies) {
        this.technologies = technologies;
    }
}
