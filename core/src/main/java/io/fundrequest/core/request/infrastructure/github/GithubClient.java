package io.fundrequest.core.request.infrastructure.github;

import io.fundrequest.core.request.infrastructure.github.parser.GithubIssueCommentsResult;
import io.fundrequest.core.request.infrastructure.github.parser.GithubResult;
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
public interface GithubClient {

    @RequestMapping(value = "/repos/{owner}/{repo}/issues/{number}", method = GET)
    GithubResult getIssue(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("number") String number
    );

    @RequestMapping(value = "/repos/{owner}/{repo}/issues/{number}/comments", method = GET)
    List<GithubIssueCommentsResult> getCommentsForIssue(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("number") String number
    );

    @RequestMapping(value = "/repos/{owner}/{repo}/issues/{number}/comments", method = POST)
    void createCommentOnIssue(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("number") String number,
            CreateGithubComment comment
    );

    @RequestMapping(value = "/repos/{owner}/{repo}/issues/comments/{commentId}", method = PATCH)
    void editCommentOnIssue(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("commentId") Long commentId,
            CreateGithubComment comment
    );

    @RequestMapping(value = "/repos/{owner}/{repo}/languages", method = GET)
    Map<String, Long> getLanguages(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );
}
