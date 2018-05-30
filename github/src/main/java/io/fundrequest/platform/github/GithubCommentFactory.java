package io.fundrequest.platform.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubCommentFactory {

    private static final String LINE_BREAK = "\r\n";
    private static final String COMMENT_HEADER =
            "[![](%1$s/requests/%2$s/badge)](%1$s/requests/%2$s)"
            + " [![](%1$s/assets/img/powered-by-fundrequest-badge.svg)](https://fundrequest.io)"
            + LINE_BREAK;

    private static final String COMMENT_FOOTER =
//            LINE_BREAK + "* For better GitHub integration please install the FundRequest [Chome browser plugin](https://chrome.google.com/webstore/search/fundrequest)."
            LINE_BREAK + "* Looking for more? Feel free to [browse](https://fundrequest.io/requests) through all funded requests.";

    private static final String FUNDED_COMMENT_TEMPLATE =
            COMMENT_HEADER
            + "This issue has been funded using [FundRequest](%1$s/requests/%2$s). A developer can claim the reward by submitting a pull request referencing this issue. "
            + "([How to Close Issues via Pull Requests?](https://help.github.com/articles/closing-issues-using-keywords)) e.g. `fixes #%3$s`"
            + LINE_BREAK
            + LINE_BREAK
            + "* For more help on how to claim on issue, please visit our [help section](https://help.fundrequest.io)."
            + COMMENT_FOOTER;

    private static final String RESOLVED_COMMENT_TEMPLATE =
            COMMENT_HEADER
//            + "Thank you @%3$s for your code contribution. You can now claim the reward that is linked to this issue. This can be done via our "
//            + "[Chome browser plugin](https://chrome.google.com/webstore/search/fundrequest) or directly from the [funded request](%1$s/requests/%2$s)."
            + "Thank you @%3$s for your code contribution. You can now claim the reward that is linked to this issue. This can be done directly from the [funded request](%1$s/requests/%2$s)."
            + LINE_BREAK
            + COMMENT_FOOTER;

    private static final String CLOSED_COMMENT_TEMPLATE =
            COMMENT_HEADER
            + "Thank you @%3$s for your code contribution. [The reward linked to this issue](%1$s/requests/%2$s) has been transferred to your account."
            + LINE_BREAK
            + COMMENT_FOOTER;

    private final String badgeBasepath;

    public GithubCommentFactory(@Value("${io.fundrequest.badge.basepath:https://fundrequest.io}") final String badgeBasepath) {
        this.badgeBasepath = badgeBasepath;
    }

    public String createFundedComment(final Long requestId, final String githubIssueNumber) {
        return String.format(FUNDED_COMMENT_TEMPLATE, badgeBasepath, requestId, githubIssueNumber);
    }

    public String createResolvedComment(final Long requestId, final String solver) {
        return create(RESOLVED_COMMENT_TEMPLATE, requestId, solver);
    }

    public String createClosedComment(final Long requestId, final String solver) {
        return create(CLOSED_COMMENT_TEMPLATE, requestId, solver);
    }

    private String create(final String commentTemplate, final Long requestId, final String solver) {
        return String.format(commentTemplate, badgeBasepath, requestId, solver);
    }
}
