package com.climb.monitoring.config;

import com.climb.monitoring.constants.CacheConstants;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {
    /**
     * 节点同步缓存的name和key的分隔符
     */
    public static final String DOUBLE_COLON = "::";
    /**
     * 默认缓存时间
     */
    private static final long DEFAULT_TTL = 30;
    /**
     * 默认最大条数
     */
    private static final long MAXIMUM_SIZE = 10000;
    /**
     * 默认分钟
     */
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> cacheBeans = getCacheBeans();
        if (ObjectUtils.isNotEmpty(cacheBeans)) {
            cacheManager.setCaches(cacheBeans);
        }
        return cacheManager;
    }

    /**
     * 把缓存CaffeineCache配置到添加到这个list中
     * @return
     */
    private List<CaffeineCache> getCacheBeans() {
        List<CaffeineCache> list = new ArrayList<>();
        list.add(CacheConfig.builderCaffeineCache(CacheConstants.TEST_CACHE));
        return list;
    }



    // 构造缓存
    public static CaffeineCache builderCaffeineCache(String name, long ttl, TimeUnit unit, long maxSize) {
        return new CaffeineCache(name, Caffeine.newBuilder().expireAfterWrite(ttl, unit).maximumSize(maxSize).build());
    }

    public static CaffeineCache builderCaffeineCache(String name, long ttl) {
        return new CaffeineCache(name, Caffeine.newBuilder().expireAfterWrite(ttl, DEFAULT_TIME_UNIT).maximumSize(MAXIMUM_SIZE).build());
    }

    public static CaffeineCache builderCaffeineCache(String name) {
        return new CaffeineCache(name, Caffeine.newBuilder().expireAfterWrite(DEFAULT_TTL, DEFAULT_TIME_UNIT).maximumSize(MAXIMUM_SIZE).build());
    }



}