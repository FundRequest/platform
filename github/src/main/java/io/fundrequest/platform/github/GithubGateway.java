package io.fundrequest.platform.github;

import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.github.parser.GithubResult;
import io.fundrequest.platform.github.parser.GithubUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GithubGateway {

    private GithubClient githubClient;

    public GithubGateway(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    @Cacheable(value = "github_issue")
    public GithubResult getIssue(String owner, String repo, String number) {
        return githubClient.getIssue(owner, repo, number);
    }

    @Cacheable(value = "github_comments")
    public List<GithubIssueCommentsResult> getCommentsForIssue(String owner, String repo, String number) {
        return githubClient.getCommentsForIssue(owner, repo, number);
    }

    public void createCommentOnIssue(String owner, String repo, String number, CreateGithubComment comment) {
        githubClient.createCommentOnIssue(owner, repo, number, comment);
    }

    public void editCommentOnIssue(String owner, String repo, Long commentId, CreateGithubComment comment) {
        githubClient.editCommentOnIssue(owner, repo, commentId, comment);
    }

    @Cacheable(value = "github_repo_languages")
    public Map<String, Long> getLanguages(String owner, String repo) {
        return githubClient.getLanguages(owner, repo);
    }

    public GithubUser getUser(String username) {
        return githubClient.getUser(username);
    }
}
