package io.fundrequest.platform.github;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(
        name = "github-raw-client",
        url = "https://raw.githubusercontent.com/"
)
interface GithubRawClient {

    @RequestMapping(value = "{owner}/{repo}/{branch}/{filePath}", method = GET)
    String getContentsAsRaw(@PathVariable("owner") String owner,
                            @PathVariable("repo") String repo,
                            @PathVariable("branch") String branch,
                            @PathVariable("filePath") String filePath);
}
