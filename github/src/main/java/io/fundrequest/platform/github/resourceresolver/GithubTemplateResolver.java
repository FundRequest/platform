package io.fundrequest.platform.github.resourceresolver;

import io.fundrequest.platform.github.GithubRawClient;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

public class GithubTemplateResolver extends AbstractConfigurableTemplateResolver {

    private final String owner;
    private final String repo;
    private final String branch;
    private final GithubRawClient githubRawClient;

    public GithubTemplateResolver(final String owner, final String repo, final String branch, final GithubRawClient githubRawClient) {
        this.owner = owner;
        this.repo = repo;
        this.branch = branch;
        this.githubRawClient = githubRawClient;
    }

    @Override
    protected ITemplateResource computeTemplateResource(final IEngineConfiguration configuration,
                                                        final String ownerTemplate,
                                                        final String template,
                                                        final String resourceName,
                                                        final String characterEncoding,
                                                        final Map<String, Object> templateResolutionAttributes) {
        return new GithubTemplateResource(owner, repo, branch, resourceName, githubRawClient);
    }
}