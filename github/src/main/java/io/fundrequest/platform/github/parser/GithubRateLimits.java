package io.fundrequest.platform.github.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class GithubRateLimits {

    private Resources resources;

    public static GithubRateLimitsBuilder with() {
        return new GithubRateLimitsBuilder();
    }

    public GithubRateLimit getCore() {
        return resources.getCore();
    }

    public GithubRateLimit getSearch() {
        return resources.getSearch();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(builderMethodName = "with")
    public static class Resources {
        private GithubRateLimit core;
        private GithubRateLimit search;
    }

    public static final class GithubRateLimitsBuilder {
        private GithubRateLimit core;
        private GithubRateLimit search;

        private GithubRateLimitsBuilder() {
        }

        public GithubRateLimitsBuilder core(final GithubRateLimit core) {
            this.core = core;
            return this;
        }

        public GithubRateLimitsBuilder search(final GithubRateLimit search) {
            this.search = search;
            return this;
        }

        public GithubRateLimits build() {
            GithubRateLimits githubRateLimits = new GithubRateLimits();
            githubRateLimits.setResources(Resources.with()
                                                   .core(core)
                                                   .search(search)
                                                   .build());
            return githubRateLimits;
        }
    }
}
