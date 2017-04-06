package io.fundrequest.core.request;

import org.hibernate.validator.constraints.NotBlank;

public class CreateRequestCommand {

    @NotBlank
    private String issueLink;
    @NotBlank
    private String label;

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
}
