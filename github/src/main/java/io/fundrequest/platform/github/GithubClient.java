package io.fundrequest.platform.github;

import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.github.parser.GithubRateLimits;
import io.fundrequest.platform.github.parser.GithubResult;
import io.fundrequest.platform.github.parser.GithubUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(
        name = "github-client",
        url = "https://api.github.com/",
        configuration = GithubFeignConfiguration.class
)
interface GithubClient {

    @RequestMapping(value = "/repos/{owner}/{repo}/issues/{number}", method = GET, headers = "Accept=application/vnd.github.html+json")
    GithubResult getIssue(@PathVariable("owner") String owner,
                          @PathVariable("repo") String repo,
                          @PathVariable("number") String number);

    @RequestMapping(value = "/repos/{owner}/{repo}/pulls/{number}", method = GET, headers = "Accept=application/vnd.github.html+json")
    GithubResult getPullrequest(@PathVariable("owner") String owner,
                                @PathVariable("repo") String repo,
                                @PathVariable("number") String number);

    @RequestMapping(value = "/repos/{owner}/{repo}/issues/{number}/comments", method = GET, headers = "Accept=application/vnd.github.html+json")
    List<GithubIssueCommentsResult> getCommentsForIssue(@PathVariable("owner") String owner,
                                                        @PathVariable("repo") String repo,
                                                        @PathVariable("number") String number);

    @RequestMapping(value = "/repos/{owner}/{repo}/issues/{number}/comments", method = POST, consumes = "application/json")
    void createCommentOnIssue(@PathVariable("owner") String owner,
                              @PathVariable("repo") String repo,
                              @PathVariable("number") String number,
                              CreateGithubComment comment);

    @RequestMapping(value = "/repos/{owner}/{repo}/issues/comments/{commentId}", method = PATCH, consumes = "application/json")
    void editCommentOnIssue(@PathVariable("owner") String owner,
                            @PathVariable("repo") String repo,
                            @PathVariable("commentId") Long commentId,
                            CreateGithubComment comment);

    @RequestMapping(value = "/repos/{owner}/{repo}/languages", method = GET)
    Map<String, Long> getLanguages(@PathVariable("owner") String owner,
                                   @PathVariable("repo") String repo);

    @RequestMapping(value = "/users/{username}", method = GET)
    GithubUser getUser(@PathVariable("username") String username);

    @RequestMapping(value = "/rate_limit", method = GET)
    GithubRateLimits getRateLimit();

}
