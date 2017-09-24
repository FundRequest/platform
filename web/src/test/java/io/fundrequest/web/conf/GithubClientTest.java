package io.fundrequest.web.conf;

import io.fundrequest.core.request.infrastructure.github.CreateGithubComment;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    public void githubAddComment() throws Exception {
        CreateGithubComment comment = new CreateGithubComment();
        comment.setBody("This is an automated comment");
        githubClient.createCommandOnIssue("FundRequest", "area51", "4", comment);
    }
}