package io.fundreqest.platform.tweb.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(
                Arrays.asList(
                        buildCache("possible_tokens", 1, HOURS),
                        buildCache("erc20.tokens.decimals", 30, DAYS),
                        buildCache("erc20.tokens.name", 30, DAYS),
                        buildCache("erc20.tokens.symbol", 30, DAYS),
                        buildCache("token_price", 15, MINUTES),
                        buildCache("funds", 7, DAYS),
                        buildCache("github_issue", 1, DAYS),
                        buildCache("github_comments", 1, DAYS),
                        buildCache("loginUserData", 1, DAYS),
                        buildCache("ref_links", 30, DAYS),
                        buildCache("user_profile", 1, DAYS),
                        buildCache("github_repo_languages", 1, DAYS),
                        buildCache("projects", 7, DAYS),
                        buildCache("technologies", 7, DAYS),
                        buildCache("statistics", 7, DAYS)
                             )
                         );
        return manager;
    }

    private CaffeineCache buildCache(String name, int ttl, TimeUnit ttlUnit) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                                               .expireAfterWrite(ttl, ttlUnit)
                                               .maximumSize(1000000)
                                               .ticker(Ticker.systemTicker())
                                               .build());
    }

}
