package io.fundrequest.platform.github.resourceresolver;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import io.fundrequest.platform.github.GithubRawClient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.Reader;
import java.io.StringReader;

@Slf4j
@Getter
@EqualsAndHashCode(exclude = {"githubRawClient", "templateContentsCache"})
public class GithubTemplateResource implements ITemplateResource {
    private static final String GITHUB = "GITHUB";

    private final String owner;
    private final String repo;
    private final String branch;
    private final String location;
    private final GithubRawClient githubRawClient;

    private String templateContentsCache;

    public GithubTemplateResource(final String owner, final String repo, final String branch, final String location, final GithubRawClient githubRawClient) {
        this.owner = owner;
        this.repo = repo;
        this.branch = branch;
        this.location = location;
        this.githubRawClient = githubRawClient;
    }

    @Override
    public String getDescription() {
        return String.format("%s/%s/%s/%s", owner, repo, branch, location);
    }

    @Override
    public String getBaseName() {
        return location;
    }

    @Override
    public boolean exists() {
        return StringUtils.isNotBlank(fetchTemplateContents());
    }

    @Override
    public Reader reader() {
        final String templateContents = fetchTemplateContents();
        return StringUtils.isNotBlank(templateContents) ? new StringReader(templateContents) : null;
    }

    private String fetchTemplateContents() {
        if (templateContentsCache == null) {
            try {
                templateContentsCache = githubRawClient.getContentsAsRaw(owner, repo, branch, location);
            } catch (HystrixRuntimeException e) {
                templateContentsCache = "";
                logHystrixRuntimeException(e);
            } catch (Exception e) {
                templateContentsCache = "";
                logError(e);
            }
        }
        return templateContentsCache;
    }

    private void logHystrixRuntimeException(HystrixRuntimeException e) {
        try {
            throw e.getCause();
        } catch (FeignException e1) {
            if (e1.status() == 404) {
                logWarn();
            } else {
                logError(e);
            }
        } catch (Throwable throwable) {
            logError(throwable);
        }
    }

    private void logWarn() {
        log.warn(String.format("Template %s/%s/%s/%s does not exist on GitHub", owner, repo, branch, location));
    }

    private void logError(Throwable e) {
        log.error(String.format("Something went wrong while fetching template %s/%s/%s/%s  from GitHub", owner, repo, branch, location), e);
    }

    @Override
    public GithubTemplateResource relative(String relativeLocation) {
        return new GithubTemplateResource(owner, repo, branch, relativeLocation, githubRawClient);
    }
}
