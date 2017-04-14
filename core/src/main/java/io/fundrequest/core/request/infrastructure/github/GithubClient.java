package io.fundrequest.core.request.infrastructure.github;

import io.fundrequest.core.request.infrastructure.github.parser.GithubResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
}
