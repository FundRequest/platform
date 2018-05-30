package io.fundrequest.platform.github;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubCommentFactoryTest {

    private final static String EXPECTED_FUNDED_COMMENT =
            "[![](https://fundrequest.io/requests/156/badge)](https://fundrequest.io/requests/156)"
            + " [![](https://fundrequest.io/assets/img/powered-by-fundrequest-badge.svg)](https://fundrequest.io)"
            + "\r\n"
            + "This issue has been funded using [FundRequest](https://fundrequest.io/requests/156). A developer can claim the reward by submitting a pull request referencing this issue. "
            + "([How to Close Issues via Pull Requests?](https://help.github.com/articles/closing-issues-using-keywords)) e.g. `fixes #8765`"
            + "\r\n"
            + "\r\n* For more help on how to claim on issue, please visit our [help section](https://help.fundrequest.io)."
//            + "\r\n* For better GitHub integration please install the FundRequest [Chome browser plugin](https://chrome.google.com/webstore/search/fundrequest)."
            + "\r\n* Looking for more? Feel free to [browse](https://fundrequest.io/requests) through all funded requests.";

    private final static String EXPECTED_RESOLVED_COMMENT =
            "[![](https://fundrequest.io/requests/635/badge)](https://fundrequest.io/requests/635)"
            + " [![](https://fundrequest.io/assets/img/powered-by-fundrequest-badge.svg)](https://fundrequest.io)"
            + "\r\n"
//            + "Thank you @dfghd-ghjgfg for your code contribution. You can now claim the reward that is linked to this issue. This can be done via our "
//            + "[Chome browser plugin](https://chrome.google.com/webstore/search/fundrequest) or directly from the [funded request](https://fundrequest.io/requests/635)."
            + "Thank you @dfghd-ghjgfg for your code contribution. You can now claim the reward that is linked to this issue. This can be done directly from the [funded request](https://fundrequest.io/requests/635)."
            + "\r\n"
//            + "\r\n* For better GitHub integration please install the FundRequest [Chome browser plugin](https://chrome.google.com/webstore/search/fundrequest)."
            + "\r\n* Looking for more? Feel free to [browse](https://fundrequest.io/requests) through all funded requests.";

    private final static String EXPECTED_CLOSED_COMMENT =
            "[![](https://fundrequest.io/requests/5473/badge)](https://fundrequest.io/requests/5473)"
            + " [![](https://fundrequest.io/assets/img/powered-by-fundrequest-badge.svg)](https://fundrequest.io)"
            + "\r\n"
            + "Thank you @ljn-ytd for your code contribution. [The reward linked to this issue](https://fundrequest.io/requests/5473) has been transferred to your account."
            + "\r\n"
//            + "\r\n* For better GitHub integration please install the FundRequest [Chome browser plugin](https://chrome.google.com/webstore/search/fundrequest)."
            + "\r\n* Looking for more? Feel free to [browse](https://fundrequest.io/requests) through all funded requests.";

    private GithubCommentFactory githubCommentFactory;

    @Before
    public void setUp() {
        githubCommentFactory = new GithubCommentFactory("https://fundrequest.io");
    }

    @Test
    public void createFundedComment() {
        final long requestId = 156;
        final String githubIssueNumber = "8765";

        final String result = githubCommentFactory.createFundedComment(requestId, githubIssueNumber);

        assertThat(result).isEqualTo(EXPECTED_FUNDED_COMMENT);
    }

    @Test
    public void createResolvedComment() {
        final long requestId = 635;
        final String solver = "dfghd-ghjgfg";

        final String result = githubCommentFactory.createResolvedComment(requestId, solver);

        assertThat(result).isEqualTo(EXPECTED_RESOLVED_COMMENT);
    }

    @Test
    public void createClosedComment() {
        final long requestId = 5473;
        final String solver = "ljn-ytd";

        final String result = githubCommentFactory.createClosedComment(requestId, solver);

        assertThat(result).isEqualTo(EXPECTED_CLOSED_COMMENT);
    }
}
