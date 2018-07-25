package io.fundrequest.core.contract.service;

import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class GetAllPossibleTokensKeyGenerator implements KeyGenerator {

    private final KeyGenerator fallbackKeyGenerator;

    public GetAllPossibleTokensKeyGenerator(final KeyGenerator simpleKeyGenerator) {
        this.fallbackKeyGenerator = simpleKeyGenerator;
    }

    @Override
    public Object generate(final Object target, final Method method, final Object... params) {
        final String platform = (String) params[0];
        if (Platform.GITHUB.name().equalsIgnoreCase(platform)) {
            return generateGitHubKey(platform, (String) params[1]);
        }
        return fallbackKeyGenerator.generate(target, method, params);
    }

    private String generateGitHubKey(final String platform, final String platformId) {
        return platform + "-" + GithubPlatformIdParser.extractOwner(platformId) + "-" + GithubPlatformIdParser.extractRepo(platformId);
    }
}
