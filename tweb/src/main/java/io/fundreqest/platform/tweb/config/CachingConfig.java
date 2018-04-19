package io.fundreqest.platform.tweb.config;

import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public ConcurrentMapCacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {

            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name,
                                              cacheBuilder(name).build().asMap(), false);
            }
        };
    }

    @NotNull
    private CacheBuilder<Object, Object> cacheBuilder(final String cacheName) {
        final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        if (cacheName != null && cacheName.startsWith("shortlived")) {
            return cacheBuilder.expireAfterWrite(5, TimeUnit.MINUTES);
        }
        if (cacheName != null && cacheName.startsWith("dayttl")) {
            return cacheBuilder.expireAfterWrite(24, TimeUnit.HOURS);
        } else {
            return cacheBuilder;
        }
    }
}
