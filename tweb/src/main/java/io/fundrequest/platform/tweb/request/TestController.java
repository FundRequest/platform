package io.fundrequest.platform.tweb.request;

import io.fundrequest.core.identity.IdentityAPIClient;
import io.fundrequest.platform.github.GithubRawClient;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final IdentityAPIClient identityAPIClient;
    private final GithubRawClient githubRawClient;

    public TestController(final IdentityAPIClient identityAPIClient, final GithubRawClient githubRawClient) {
        this.identityAPIClient = identityAPIClient;
        this.githubRawClient = githubRawClient;
    }

    @GetMapping("/test1")
    public UserRepresentation test() {
        return identityAPIClient.findByIdentityProviderAndFederatedUsername("github", "nico-ptrs");
    }

    @GetMapping("/test2")
    public String test1() {
        return githubRawClient.getContentsAsRaw("fundrequest", "content-management", "master", "notification-templates/open-requests.html");
    }
}
