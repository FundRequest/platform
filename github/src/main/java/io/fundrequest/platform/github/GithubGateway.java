package io.fundrequest.platform.github;

import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.github.parser.GithubRateLimits;
import io.fundrequest.platform.github.parser.GithubResult;
import io.fundrequest.platform.github.parser.GithubUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GithubGateway {

    private final GithubApiClient githubApiClient;
    private final GithubRawClient githubRawClient;


    public GithubGateway(final GithubApiClient githubApiClient, final GithubRawClient githubRawClient) {
        this.githubApiClient = githubApiClient;
        this.githubRawClient = githubRawClient;
    }

    @Cacheable(value = "github_issue")
    public GithubResult getIssue(String owner, String repo, String number) {
        return githubApiClient.getIssue(owner, repo, number);
    }

    @CacheEvict(value = "github_issue")
    public void evictIssue(String owner, String repo, String number) {
        // Intentionally blank
    }

    public GithubResult getPullrequest(String owner, String repo, String number) {
        return githubApiClient.getPullrequest(owner, repo, number);
    }

    @Cacheable(value = "github_comments")
    public List<GithubIssueCommentsResult> getCommentsForIssue(String owner, String repo, String number) {
        return githubApiClient.getCommentsForIssue(owner, repo, number);
    }

    @CacheEvict(value = "github_comments")
    public void evictCommentsForIssue(String owner, String repo, String number) {
        // Intentionally blank
    }

    public void createCommentOnIssue(String owner, String repo, String number, CreateGithubComment comment) {
        githubApiClient.createCommentOnIssue(owner, repo, number, comment);
    }

    public void editCommentOnIssue(String owner, String repo, Long commentId, CreateGithubComment comment) {
        githubApiClient.editCommentOnIssue(owner, repo, commentId, comment);
    }

    @Cacheable(value = "github_repo_languages")
    public Map<String, Long> getLanguages(String owner, String repo) {
        return githubApiClient.getLanguages(owner, repo);
    }

    public GithubUser getUser(String username) {
        return githubApiClient.getUser(username);
    }

    public GithubRateLimits getRateLimit() {
        return githubApiClient.getRateLimit();
    }

    public String getContentsAsRaw(final String owner, final String repo, final String branch, final String filePath) {
        return githubRawClient.getContentsAsRaw(owner, repo, branch, filePath);
    }

    public String getContentsAsHtml(final String owner, final String repo, final String branch, final String filePath) {
        return githubApiClient.getContentsAsHtml(owner, repo, branch, filePath);
    }
}
