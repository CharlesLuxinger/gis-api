package com.github.charlesluxinger.gisapi.infra.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Charles Luxinger
 * @version 1.0.0 13/12/20
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CACHE_NAME = "gis-api";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_NAME);
    }

    @Bean
    public KeyGenerator findNearbyAndCoverageAreaKeyGenerator() {
        return new FindNearbyAndCoverageAreaKeyGenerator();
    }

}
