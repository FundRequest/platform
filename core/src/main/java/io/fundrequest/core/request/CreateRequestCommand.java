package io.fundrequest.core.request;

import io.fundrequest.core.request.validation.GithubLink;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class CreateRequestCommand {

    public String getIssueLink() {
        return issueLink;
    }


    @NotBlank
    @Length(min = 20, max = 2000)
    @GithubLink
    private String issueLink;

    public void setIssueLink(String issueLink) {
        this.issueLink = issueLink;
    }

}
